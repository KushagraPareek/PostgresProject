CREATE OR REPLACE RECURSIVE VIEW indirectOwns(id, own_id, fraction) AS( 
SELECT * FROM directOwns 
UNION  
SELECT D.id , I.own_id, ROUND(D.fraction * I.fraction , 25) 
FROM directOwns D, indirectOwns I WHERE D.own_id = I.id AND D.id <> I.own_id);

CREATE OR REPLACE VIEW directpOwns AS 
(SELECT P.p_id AS id, O.c_id AS own_id, ROUND((O.amount * 1.0/get_shares(O.c_id)),25) AS 
fraction 
From person_details P, unnest(P.owns) AS O(c_id, amount)  
WHERE O.amount > 0); 

CREATE OR REPLACE RECURSIVE VIEW indirectpOwns(id, own_id, fraction) AS( 
SELECT * FROM directpOwns 
UNION  
SELECT D.id , I.own_id, ROUND(D.fraction * I.fraction , 25) 
FROM indirectpOwns D, indirectOwns I WHERE D.own_id = I.id );   


CREATE OR REPLACE VIEW tb AS ( 
select id, own_id , sum(fraction) AS frac from indirectpOwns 
GROUP BY id,own_id ); 
