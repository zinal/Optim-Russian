function appendFioString(full, next)
	if (next==nil) then
	    return full
	end
	if (string.len(next)==0) then
		return full
	end
	if (string.len(full)==0) then
		return next
	end
	return full .. " " .. next    
end

function assignFioField(target, fldname, oldVal, newVal)
	if (oldVal==nil) then
		print("... Leaving NULL " .. fldname)
		target.column.setvalue(fldname, oldVal)
	else
		if (string.len(oldVal)==0) then
			print("... Leaving empty " .. fldname)
			target.column.setvalue(fldname, oldVal)
		else
			print("... Setting " .. fldname .. " to " .. newVal)
			target.column.setvalue(fldname, newVal)
		end
	end
end

function getSafeValue(source, cname)
	tcode = source.column.gettype(cname)
	if (tcode=='VARCHAR') then
		tmp = source.column.getvalue(cname)
		if (tmp==nil) then
			return ""
		end
		return tmp
	else
		tmp = source.column.getasdouble(cname)
		if (tmp==nil) then
			return -1
		end
		return tmp
	end
end

local mvz_conn = 'LIB=db2luw,USER=optimcfg,PASS=Passw0rd,CONN=optimcfg'

function cm_transform()
	local tabName = source.gettablename()
	if (tabName=="N31") then
		local nameLast = getSafeValue(source, "N31NAMF")
		local nameFirst = getSafeValue(source, "N31NAMI")
		local nameMiddle = getSafeValue(source, "N31NAMO")
		local sexCode = getSafeValue(source, "N31TSEX")
		local nameFull = "";
		nameFull = appendFioString(nameFull, nameLast)
		nameFull = appendFioString(nameFull, nameFirst)
		nameFull = appendFioString(nameFull, nameMiddle)
		nameFull = string.upper(nameFull)
		local sourceId = "convz.optim_fio_m"
		if (sexCode==0) then
			sourceId = "convz.optim_fio_f"
		end
		local syntax = 'PRO=hash_lookup,HASHFLD="id"' ..
			',SOURCE="N31CINN",DEST="N31NAMF,N31NAMI,N31NAMO"' ..
			',REPLACE="nl,nf,ns",ID="' .. sourceId .. '",' .. mvz_conn .. 
			',FLDDEF1=(NAME="N31CINN",DT=varchar_sz)' ..
			',FLDDEF2=(NAME="N31NAMF",DT=varchar_sz)' ..
			',FLDDEF3=(NAME="N31NAMI",DT=varchar_sz)' ..
			',FLDDEF4=(NAME="N31NAMO",DT=varchar_sz)'
		print(" *** SOURCE: " .. nameFull)
		print(" *** SYNTAX: " .. syntax)
		newLast, newFirst, newMiddle = optimmask(nameFull, nameLast, nameFirst, nameMiddle, syntax)
		assignFioField(target, "N31NAMF", nameLast, newLast)
		assignFioField(target, "N31NAMI", nameFirst, newFirst)
		assignFioField(target, "N31NAMO", nameMiddle, newMiddle)
	else
		rejectrow()
	end
end
