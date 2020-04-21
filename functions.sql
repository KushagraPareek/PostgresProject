CREATE OR REPLACE FUNCTION get_company_name(id varchar(20)) 
RETURNS text as $$ 
BEGIN 
RETURN (SELECT name from company_details where id = c_id); 
END; $$ 
LANGUAGE plpgsql;   


CREATE OR REPLACE FUNCTION get_share_price(s varchar(20)) 
RETURNS bigint AS $$ 
BEGIN  
RETURN (SELECT share_price from company_details where c_id = s);  
END; $$ 
LANGUAGE plpgsql; 


CREATE OR REPLACE FUNCTION get_person_name(id varchar(20)) 
RETURNS text as $$ 
BEGIN 
RETURN (SELECT name from person_details where p_id = id); 
END; $$ 
LANGUAGE plpgsql; 


CREATE OR REPLACE FUNCTION get_shares(id varchar(20)) 
RETURNS bigint as $$ 
BEGIN 
RETURN (SELECT shares from company_details where id = c_id); 
END; $$ 
LANGUAGE plpgsql; 
