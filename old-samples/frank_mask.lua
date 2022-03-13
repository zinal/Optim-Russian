-- This script will mask the OPTIM_CUSTOMERS.CUSTNAME column using a HASH lookup
-- The lookup table to be used is decided by the value in the OPTIM_CUSTOMERS.STATE  
-- column.
--
-- If STATE = 'CO' then perform hash lookup on CUSTNAME column
--
-- This script also will need changes based on the names of your
-- lookup tables, database type, credentials, etc.  In this example,
-- db2admin is the Userid; passw0rd is the Password, and        
-- DB2UTF8A is the connect string; DB2LUW is the database type; FIRSTNAME is
-- the column name in the lookup table for replacement data; and its
-- datatype is VARCHAR with a length of 60 must code WVARCHAR_SZ for Lua 
-- with a length of 20 to match CUSTNAME column length in OPTIM_CUSTOMERS table.
-- The lookup table is DB2UTF8A.DB2ADMIN.OPTIM_UK_LASTNAME
--
 
 
function cm_transform()
	oldvalue = source.column.getvalue()
	printColvalues = true
	if (not colinfoshown) then
	   colinfoshown = true
	   print("  Processing column " .. source.column.getname())
	   print("     Type: " ..  source.column.gettype())
	   print("     Length: " .. source.column.getlength())
	end
	stateval = source.column.getvalue('STATE')
	
	if stateval == 'BL' then
		--     this section builds the parameters for the HASH Lookup ODPP function    
		--     and runs it against the old input value for the CUSTNAME column
		mask_params = 'PRO=HASH_LOOKUP,LIB=DB2LUW,ID="DB2ADMIN.OPTIM_US_LASTNAME",'
		mask_params = mask_params .. 'SOURCE="CUSTNAME",REPLACE="LASTNAME",HASHFLD=SEQ,'
		---FK CONN = DB NAME !!
		mask_params = mask_params .. 'CONN=F113P138,USER=DB2ADMIN,PASS="pwd",'
		mask_params = mask_params .. 'ALGO=SHA256,SEED="7",'
		mask_params = mask_params .. 'FLDDEF1=(NAME="CUSTNAME",DATATYPE=WVARCHAR_SZ,LEN=120),'
		mask_params = mask_params .. 'FLDDEF2=(NAME="LASTNAME",DATATYPE=WVARCHAR_SZ,LEN=120)'
		if (not mask_paramsinfoshown) then
			mask_paramsinfoshown = true
			print ("mask_params: " .. mask_params)
		end
		newvalue = optimmask(oldvalue,mask_params)
		if ( printColvalues ) then
			print ("oldvalue: " .. oldvalue)
			print ("newvalue: " .. newvalue)
		end
	else
		newvalue = oldvalue       
	end
	
	target.column.setvalue(newvalue)
end
