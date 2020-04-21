/****************************************************************************
CSE532 -- Project 2
File name: Query.java
Author: KUSHAGRA PAREEK (112551443)
Brief description: Servlet to output queries on the web
****************************************************************************/

/**
I pledge my honor that all parts
of this project were done by me alone and without collaboration with
anybody else.
**/

package com.kush.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class Query extends HttpServlet{
	
 	PostgresConnector connector = null;
	Connection conn = null;
	@Override 
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
    	
   
        if(conn == null) {
          connector = new PostgresConnector();
  		  conn = connector.connect();
  		  executeFunctions();
        }
        
        String qry = request.getParameter("param");
        response.setContentType("text/html");
        PrintWriter out = null;
        
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String rs = null;
    	switch(qry) {
    	
    	  case "1" :
    		  rs = getCompanies();
     		  break;
    	  case "2" :
    		  rs = getNetworth();
    		  break;
    	  case "3" :
    		  rs = getTopBoardMember();
    		  break;
    	  case "4" :
    		  rs = getDomination();
    		  break;
    	  case "5" :
    		  rs = getPercentage();
    		  break;
          		  
        }
    	
      if(out != null) {	
    	out.print(rs);
	    out.close();
      }
    	
    }
	
	
	
	private void executeFunctions() {
		
		String func = "CREATE OR REPLACE FUNCTION get_company_name(id varchar(20)) " + 
				"RETURNS text as $$ " + 
				"BEGIN " + 
				" RETURN (SELECT name from company_details where id = c_id); " + 
				"END; $$ " + 
				"LANGUAGE plpgsql;" +
				
				"CREATE OR REPLACE FUNCTION get_share_price(s varchar(20)) " + 
				"RETURNS bigint AS $$ " + 
				"BEGIN " + 
				" RETURN (SELECT share_price from company_details where c_id = s); " + 
				"END; $$ " + 
				"LANGUAGE plpgsql;" +
				
				"CREATE OR REPLACE FUNCTION get_person_name(id varchar(20)) " + 
				"RETURNS text as $$ " + 
				"BEGIN" + 
				" RETURN (SELECT name from person_details where p_id = id); " + 
				"END; $$ " + 
				"LANGUAGE plpgsql;" +
				
				//FOR QUERY 5
				"CREATE OR REPLACE FUNCTION get_shares(id varchar(20)) " + 
				"RETURNS bigint as $$ " + 
				"BEGIN " + 
				" RETURN (SELECT shares from company_details where id = c_id); " + 
				"END; $$ " + 
				"LANGUAGE plpgsql; " + 
				 
				"CREATE OR REPLACE VIEW directOwns AS " + 
				"(SELECT C.c_id AS id, O.c_id AS own_id, ROUND((O.amount * 1.0/get_shares(O.c_id)),25) AS fraction " + 
				"From company_details C, unnest(C.owns) AS O(c_id, amount) " + 
				"WHERE O.amount > 0); " + 
				
				"CREATE OR REPLACE RECURSIVE VIEW  indirectOwns(id, own_id, fraction) AS( " + 
				"            SELECT * FROM directOwns " + 
				"            UNION " + 
				"            SELECT D.id , I.own_id, ROUND(D.fraction * I.fraction , 25) " + 
				"            FROM directOwns D, indirectOwns I " + 
				"            WHERE D.own_id = I.id AND D.id <> I.own_id); " + 
				
				"CREATE OR REPLACE VIEW directpOwns AS " + 
				"(SELECT P.p_id AS id, O.c_id AS own_id, ROUND((O.amount * 1.0/get_shares(O.c_id)),25) AS fraction " + 
				"From person_details P, unnest(P.owns) AS O(c_id, amount) " + 
				"WHERE O.amount > 0); " + 
				 
				 
				"CREATE OR REPLACE RECURSIVE VIEW  indirectpOwns(id, own_id, fraction) AS( " + 
				"            SELECT * FROM directpOwns " + 
				"            UNION  " + 
				"            SELECT D.id , I.own_id, ROUND(D.fraction * I.fraction , 25) " + 
				"            FROM indirectpOwns D, indirectOwns I  " + 
				"            WHERE D.own_id = I.id );  " + 
				
				
				"CREATE OR REPLACE VIEW tb AS ( " + 
				"select id, own_id , sum(fraction) AS frac from indirectpOwns " + 
				" GROUP BY id,own_id ); ";			
				
		
		Statement stmt = null;
		try {
			if(conn != null) {
			  stmt = conn.createStatement();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(stmt != null) {
			try {
				 stmt.execute(func);
			}catch(SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
//Query 1	
	private String getHtml() {
		
		return "<html>"
        		+"<head>" + 
        		"<style>" + 
        		"table, th, td {" + 
        		"  border: 1px solid black; " + 
        		"  border-collapse: collapse; " + 
        		"} " + 
        		"th, td { " + 
        		"  padding: 5px; " + 
        		"  text-align: left; " + 
        		"} " + 
        		"</style> "+
        		"</head><body>";
	 }
	
	private String getCompanies() {
	
 
	    String query	 = "SELECT get_company_name(O.c_id) AS company_name " + 
				"FROM person_details P, unnest(P.owns) AS O(c_id, amount), unnest(P.board_member) AS B(id) " + 
				"where O.c_id = B.id and O.amount > 0 ; ";
	   
        Statement stmt = null;
        ResultSet res = null;
        String html = getHtml() + "<h3>Companies that are owned by one of their board members</h3>" +
                "<table style = \"width:100%\">" + 
                "  <tr>" + 
                "    <th>CompanyName</th>" + 
                "  </tr>";
        
		try {
			if(conn != null) {
			  stmt = conn.createStatement();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(stmt != null) {
			try {
				 res = stmt.executeQuery(query);
				 String additiveRes = "";
				 
				 if(res != null) {
						
						try {
							while(res.next()) {
								String name  = res.getString("company_name");
								additiveRes += "<tr>"+
								               "<td>" + name + "</td>"+
										       "</tr>";
							}
						} catch (SQLException e) {
							
							e.printStackTrace();
						}finally {
							res.close();
						}
						
						html = html + additiveRes + "</table></body></html>";
					}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				 try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		 return html;
	}
	
	
//Query 2	
	
	private String getNetworth() {
		
		
		String query = "SELECT P.name AS name , sum(O.amount * get_share_price(O.c_id)) AS networth FROM " + 
				"person_details P, unnest(P.owns) AS O(c_id, amount) " + 
				"WHERE O.amount > 0 " + 
				"GROUP BY P.p_id ;";
		
		
		
		Statement stmt = null;
        ResultSet res = null;
        String html = getHtml() + "<h1>Networth of Each Person</h1>" +
                                "<table style = \"width:100%\">" + 
                                "  <tr>" + 
                                "    <th>PersonName</th>" + 
                                "    <th>Networth</th>" + 
                                "  </tr>";
        		  
        		
		
        
        try {
			if(conn != null) {
			  stmt = conn.createStatement();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(stmt != null) {
			try {
				 res = stmt.executeQuery(query);
				 String additiveRes = "";
				 
				 if(res != null) {
						
						try {
							while(res.next()) {
								
								String name =     res.getString("name");
								String networth = Integer.toString(res.getInt("networth"));
								additiveRes  += "<tr>" +
								                "<td>" + name     + "</td>" +
										        "<td>" + networth + "</td>"+
								                "</tr>";
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}finally {
							res.close();
						}
						
						html = html + additiveRes + "</table></body></html>";
					}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				 try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
        
	
        return html;
	}
	
	//Query 3
	private String getTopBoardMember() {
		
		
	   String query =  "SELECT top_members.company , top_members.TopBoardMember " + 
	   		"FROM  " + 
	   		"(SELECT C.name AS company, get_person_name(O.p_id) AS TopBoardMember, max(O.amount) " + 
	   		"FROM company_details C, unnest(C.owned_by) AS O(p_id, amount), unnest(C.board) AS B(id) " + 
	   		"WHERE O.p_id = B.id and O.amount > 0 " + 
	   		"GROUP BY C.c_id, O.p_id) top_members";	
	   
	   
	   Statement stmt = null;
       ResultSet res = null;
       String html = getHtml() + "<h1>Top Board Members</h1>" +
                               "<table style = \"width:100%\">" + 
                               "  <tr>" + 
                               "    <th>Company Name</th>" + 
                               "    <th>Person Name</th>" + 
                               "  </tr>";
       		  
       		
		
       
       try {
			if(conn != null) {
			  stmt = conn.createStatement();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(stmt != null) {
			try {
				 res = stmt.executeQuery(query);
				 String additiveRes = "";
				 
				 if(res != null) {
						
						try {
							while(res.next()) {
								
								String name =     res.getString("company");
								String member =   res.getString("TopBoardMember");
								additiveRes  += "<tr>" +
								                "<td>" + name     + "</td>" +
										        "<td>" + member + "</td>"+
								                "</tr>";
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}finally {
							res.close();
						}
						
						html = html + additiveRes + "</table></body></html>";
					}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				 try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
       
	
       return html;
	}
	
	
	//Query 4
	
	private String getDomination(){
		
		String query = "SELECT c1.name AS cname1 , c2.name AS cname2 " + 
				"FROM company_details c1, company_details c2 " + 
				"WHERE c1.c_id <> c2.c_id " + 
				"AND  " + 
				"    EXISTS( SELECT 1 FROM  " + 
				"      unnest(c1.industry) ind1, unnest(c2.industry) ind2 " + 
				"      WHERE ind1 = ind2) AND  " + 
				"      NOT EXISTS ( ( " + 
		 
				"		      SELECT p.p_id , p.cmp FROM " + 
				"		       (SELECT p_id , O.cmp, O.shrs FROM " + 
				"			 person_details, unnest(owns) AS O(cmp, shrs) ) p " + 
				"		      WHERE p.p_id = ANY(c2.board) and p.shrs > 0 )  " + 
 
				"		      EXCEPT ( " + 
				"			       SELECT p2.p_id , p2.cmp FROM " + 
				"			       ( SELECT p_id, O.cmp, O.shrs " + 
				"				 FROM person_details, unnest(owns) AS O(cmp, shrs) ) p2 " + 
				"			       WHERE p2.p_id = ANY(c2.board) and p2.shrs > 0 AND " + 
				"			       p2.shrs  <= ANY( SELECT p1.shrs FROM " + 
				"						( SELECT p_id, O.cmp, O.shrs " + 
				"						  FROM person_details, unnest(owns) AS O(cmp , shrs) ) p1 " + 
				"						  WHERE p1.p_id = ANY(c1.board) AND  " + 
				"						  p2.cmp = p1.cmp  AND p1.shrs > 0 ) ) ) ; ";
		
		
		
		   Statement stmt = null;
	       ResultSet res = null;
	       String html = getHtml() + "<h1>Company1 Dominates Company2</h1>" +
	                               "<table style = \"width:100%\">" + 
	                               "  <tr>" + 
	                               "    <th>Company 1</th>" + 
	                               "    <th>Company 2</th>" + 
	                               "  </tr>";
	       		  
	       		
			
	       
	       try {
				if(conn != null) {
				  stmt = conn.createStatement();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if(stmt != null) {
				try {
					 res = stmt.executeQuery(query);
					 String additiveRes = "";
					 
					 if(res != null) {
							
							try {
								while(res.next()) {
									
									String  cname1 =      res.getString("cname1");
									String  cname2 =   res.getString("cname2");
									additiveRes  += "<tr>" +
									                "<td>" + cname1   + "</td>" +
											        "<td>" + cname2 + "</td>"+
									                "</tr>";
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}finally {
								res.close();
							}
							
							html = html + additiveRes + "</table></body></html>";
						}
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					 try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
	       
		
	       return html;
		
	}
	
	//Query 5
	private String getPercentage() {
			
		String query = "select get_person_name(id) AS NAME, get_company_name(own_id) AS CNAME, "
				+ "(frac*100) AS PERCENT from tb where frac > 0.1 " ;
		
		
		
		Statement stmt = null;
	       ResultSet res = null;
	       String html = getHtml() + "<h1>Control Percentage</h1>" +
	                               "<table style = \"width:100%\">" + 
	                               "  <tr>" + 
	                               "    <th>Person  Name</th>" + 
	                               "    <th>Company Name</th>" + 
	                               "	<th>Percentage</th>"   +
	                               "  </tr>";
	       		  
	       		
			
	       
	       try {
				if(conn != null) {
				  stmt = conn.createStatement();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if(stmt != null) {
				try {
					 res = stmt.executeQuery(query);
					 String additiveRes = "";
					 
					 if(res != null) {
							
							try {
								while(res.next()) {
									
									String name =     res.getString("NAME");
									String company =   res.getString("CNAME");
									String percentage = Double.toString(res.getDouble("PERCENT"));
									additiveRes  += "<tr>" +
									                "<td>" + name   + "</td>" +
											        "<td>" + company + "</td>"+
											        "<td>" + percentage + "</td>"+
									                "</tr>";
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}finally {
								res.close();
							}
							
							html = html + additiveRes + "</table></body></html>";
						}
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					 try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
	       
		
	       return html;
		
	}
	
}
