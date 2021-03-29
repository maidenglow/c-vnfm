<%@ page contentType="text/html; charset=UTF-8" %>
<script src="scripts/jquery-1.9.0.min.js" type="text/javascript"></script>
<script src="scripts/jquery-ui.js" type="text/javascript"></script>
<link href="styles/skin/ui.dynatree.css" rel="stylesheet" type="text/css">
<link href="styles/pocketgrid.css" rel="stylesheet" type="text/css">
<script src="scripts/jquery.dynatree.min.js" type="text/javascript"></script>
<script src="scripts/taffy-min.js" type="text/javascript"></script>
	<script>
	var response;
	var title;
	var titleUrl;
	var qos_ref_no;
	var connection_ref_no;
	var vld_security_ref_no;
	var vld_no;
	var version_no;
	var HtmlStr='';
	
	
	  $(function(){
		jQuery.ajax({
   	        type:"GET",
   	        async: true,
   	        url:"vld.do",
   	        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
   	        success : function(data) {
   	        	treeView(data);
  	        }
  	     });
	  });
	  
	  function treeView(data){
			var response2 = eval(data.trim());
		    $("#tree").dynatree({
		    	onActivate: function(node) {
		        if(node.data.title=="security"||node.data.title.substring(0,3)=="vld"){
		        	deleteSet();
		        	if(node.data.title.substring(0,3)=="vld"&&node.data.title.length>3){
		        		titleUrl="vld0";
		        	}else{
		        		titleUrl=node.data.title;
		        	}
		        	title=node.data.title;
			    	jQuery.ajax({
			    	      type:"GET",
			    	      async: true,
			    	      url:titleUrl+".do",
			    	      data: {title:title},
			    	      contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			    	      success : function(data) {
			    	      	response = eval(data.trim());
			    	       	htmlSelect(titleUrl,"read");
			    	      },
			    	      complete : function(data) {
			    	      },
			    	      error : function(xhr, status, error) {
			    	            alert("에러발생");
			    	      }
			    	  }); 
		        }
		      },
		      children: response2
		    });
		}
	  
	  function update(id){//option security
		  jQuery.ajax({
  	        type:"GET",
  	        async: true,
  	        url:"securityUpdate.do",
  	        data: {security_no:id,item:$("#"+id).val()},
  	        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
  	        success : function(data) {
  	        	response = eval(data.trim());
  	          	htmlSelect("security","read");
  	          	alert("저장 되었습니다.");
  	        },
  	        complete : function(data) {
  	        },
  	        error : function(xhr, status, error) {
  	              alert("에러발생");
  	        }
  	    }); 
	  }
	  function save(){//option security
		  if($("#add").val()==''){
			  alert("입력되지 않았습니다.");
			  return false;
		  }
		  jQuery.ajax({
  	        type:"GET",
  	        async: true,
  	        url:"securitySave.do",
  	        data: {item:$("#add").val()},
  	        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
  	        success : function(data) {
  	        	response = eval(data.trim());
  	          	htmlSelect("security","read");
  	          	alert("저장 되었습니다.");
  	        },
  	        complete : function(data) {
  	        },
  	        error : function(xhr, status, error) {
  	              alert("에러발생");
  	        }
  	    }); 
		  deleteSet();
	  }
	  function delete1(id){//option security
		  jQuery.ajax({
  	        type:"GET",
  	        async: true,
  	        url:"securityDelete.do",
  	        data: {security_no:id},
  	        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
  	        success : function(data) {
  	        	response = eval(data.trim());
  	          	htmlSelect("security","read");
  	          	alert("저장 되었습니다.");
  	        },
  	        complete : function(data) {
  	        },
  	        error : function(xhr, status, error) {
  	              alert("에러발생");
  	        }
  	    }); 
	  }
	  function deleteSet(){
		  	var str='';
		    	var chartParent = $('#createInput2').parent();
		      $('#createInput2').remove();
		      $('<table id=createInput2 class="table">'+str+'</table>').appendTo(chartParent);
		  }
	  function treeSet(){
		  var str='';
  			var chartParent = $('#createInput').parent();
	        $('#createInput').remove();
	        $('<table id=createInput class="table">'+str+'</table>').appendTo(chartParent);
	      
	        var chartParent2 = $('#tree').parent();
	        $('#tree').remove();
	        $('<div id=tree>'+str+'</div>').appendTo(chartParent2);
	  }
	  function add(){//option security
		  	var str='';
		  
			        	str+='<tr>';
			        	str+='<td>';
			        		str+='<input type=text id=add  class="form-control ng-pristine ng-invalid ng-invalid-required">';
			        	str+='</td>';
			        	str+='<td><input type="button" value="저장" class="btn btn-default btn-w-xs" onclick="javascript:save();">';
			        	str+='<input type="button" value="삭제" class="btn btn-default btn-w-xs" onclick="javascript:deleteSet();"></td>';
			        	str+='</tr>';
		    	var chartParent = $('#createInput2').parent();
		      $('#createInput2').remove();
		      $('<table id=createInput2 class="table">'+str+'</table>').appendTo(chartParent);
		  }
	  function read(){
		  jQuery.ajax({
  	        type:"GET",
  	        async: true,
  	      	url:titleUrl+".do",
  	      	data: {title:title},
  	        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
  	        success : function(data) {
  	        	response = eval(data.trim());
  	          	htmlSelect(titleUrl,"update");
  	        },
  	        complete : function(data) {
  	              // 통신이 실패했어도 완료가 되었을 때 이 함수를 타게 된다.
  	              // TODO
  	        },
  	        error : function(xhr, status, error) {
  	              alert("에러발생");
  	        }
  	    }); 
	  }
	  function vldInsertPageGo(){
		  jQuery.ajax({
	  	        type:"GET",
	  	        async: true,
	  	      	url:"vldInsertPageGo.do",
	  	      	data: {title:title},
	  	        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	  	        success : function(data) {
	  	        	response = eval(data.trim());
	  	          	htmlSelect(titleUrl,"update");
	  	        },
	  	        complete : function(data) {
	  	              // 통신이 실패했어도 완료가 되었을 때 이 함수를 타게 된다.
	  	              // TODO
	  	        },
	  	        error : function(xhr, status, error) {
	  	              alert("에러발생");
	  	        }
	  	    }); 
	  }
	  function xmlUpdate(){
		  if($("#number_of_endpoints").val()==""||$("#root_requirement").val()==""||$("#test_access").val()==""||$("#connectivity_type").val()==""){
			  	alert("필수 입력 입니다.");
			  	if($("#number_of_endpoints").val()=="")$("#number_of_endpoints").focus();
			  	else if($("#root_requirement").val()=="")$("#root_requirement").focus();
			  	else if($("#test_access").val()=="")$("#test_access").focus();
			  	else if($("#connectivity_type").val()=="")$("#connectivity_type").focus();
			  	return false;
			  }
		  	var qos ='';
		    var list = $("input[name='qos']");
		    for(var i = 0; i < list.length; i++){
		        if(list[i].checked){ //선택되어 있으면 배열에 값을 저장함
		        	qos+=list[i].value+',';
		        }
		    }
		    var connection ='';
		    var list = $("input[name='connection']");
		    for(var i = 0; i < list.length; i++){
		        if(list[i].checked){ //선택되어 있으면 배열에 값을 저장함
		        	connection+=list[i].value+',';
		        }
		    }
		    var vld_security=$(':radio[name="vld_security"]:checked').val();
		    
		    jQuery.ajax({
	  	        type:"POST",
	  	        async: true,
	  	        url:"vldUpdate.do",
	  	        data: {
	  	        		number_of_endpoints:$("#number_of_endpoints").val(),
	  	        		root_requirement:$("#root_requirement").val(),
	  	        		leaf_requirement:$("#leaf_requirement").val(),
	  	        		qos:qos,
	  	        		test_access:$("#test_access").val(),
	  	        		connection:connection,
	  	        		connectivity_type:$("#connectivity_type").val(),
	  	        		vld_security:vld_security,
	  	        		title:title,
	  	        		qos_ref_no:qos_ref_no,
	  	        		connection_ref_no:connection_ref_no,
	  	        		vld_security_ref_no:vld_security_ref_no,
	  	        		vld_no:vld_no,
	  	        		version_no:version_no
	  	        		},
	  	        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	  	        success : function(data) {
	  	        	response = eval(data.trim());
	  	          	htmlSelect(titleUrl,"update");
	  	          	alert("저장 되었습니다.");
	  	        },
	  	        complete : function(data) {
	  	              // 통신이 실패했어도 완료가 되었을 때 이 함수를 타게 된다.
	  	              // TODO
	  	        },
	  	        error : function(xhr, status, error) {
	  	              alert("에러발생");
	  	        }
	  	    }); 
	  }
	  function xmlSave(){
		  if($("#vld_id").val()==""||$("#vendor").val()==""||$("#descriptor_version").val()==""||$("#number_of_endpoints").val()==""||$("#root_requirement").val()==""||$("#test_access").val()==""||$("#connectivity_type").val()==""){
		  	alert("필수 입력 입니다.");
		  	if($("#vld_id").val()=="")$("#vld_id").focus();
		  	else if($("#vendor").val()=="")$("#vendor").focus();
		  	else if($("#descriptor_version").val()=="")$("#descriptor_version").focus();
		  	else if($("#number_of_endpoints").val()=="")$("#number_of_endpoints").focus();
		  	else if($("#root_requirement").val()=="")$("#root_requirement").focus();
		  	else if($("#test_access").val()=="")$("#test_access").focus();
		  	else if($("#connectivity_type").val()=="")$("#connectivity_type").focus();
		  	return false;
		  }
		  if($("#vld_id").val().length<4){
			  alert("vld로 시작하려면 4자리 이상이어야 합니다.");
			  $("#vld_id").focus();
			  return false;
		  }
		  if($("#vld_id").val().substring(0,3)!="vld"){
			  alert("vld로 시작 해야 합니다.");
			  $("#vld_id").focus();
			  return false;
		  }
		  	var qos ='';
		    var list = $("input[name='qos']");
		    for(var i = 0; i < list.length; i++){
		        if(list[i].checked){ //선택되어 있으면 배열에 값을 저장함
		        	qos+=list[i].value+',';
		        }
		    }
		    var connection ='';
		    var list = $("input[name='connection']");
		    var conLength =0;
		    for(var i = 0; i < list.length; i++){
		        if(list[i].checked){ //선택되어 있으면 배열에 값을 저장함
		        	connection+=list[i].value+',';
		        	conLength++;
		        }
		    }
		    
		    if(conLength<2){
		    	alert("connection은 두개 이상 선택 하셔야 합니다.");
		    	return false;
		    }
		    var vld_security=$(':radio[name="vld_security"]:checked').val();
		    jQuery.ajax({
	  	        type:"POST",
	  	        async: true,
	  	        url:"vldSave.do",
	  	        data: {
		  	        	vld_id:$("#vld_id").val(),
		  	        	vendor:$("#vendor").val(),
		  	        	descriptor_version:$("#descriptor_version").val(),
	  	        		number_of_endpoints:$("#number_of_endpoints").val(),
	  	        		root_requirement:$("#root_requirement").val(),
	  	        		leaf_requirement:$("#leaf_requirement").val(),
	  	        		qos:qos,
	  	        		test_access:$("#test_access").val(),
	  	        		connection:connection,
	  	        		connectivity_type:$("#connectivity_type").val(),
	  	        		vld_security:vld_security,
	  	        		title:title
	  	        		},
	  	        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	  	        success : function(data) {
  	        		treeSet();
  	        		treeView(data);
	  	          	alert("저장 되었습니다.");
	  	        },
	  	        complete : function(data) {
	  	              // 통신이 실패했어도 완료가 되었을 때 이 함수를 타게 된다.
	  	              // TODO
	  	        },
	  	        error : function(xhr, status, error) {
	  	              alert("에러발생");
	  	        }
	  	    }); 
	  }
	  
	  
	  function vldDelete(){
		  if (confirm("정말 삭제 하시겠습니까?"))
		  jQuery.ajax({
	  	        type:"POST",
	  	        async: true,
	  	        url:"vldDelete.do",
	  	        data: {
	  	        		vld_no:vld_no,
  	        			version_no:version_no,
	  	        		qos_ref_no:qos_ref_no,
	  	        		connection_ref_no:connection_ref_no,
	  	        		vld_security_ref_no:vld_security_ref_no
	  	        		
	  	        		},
	  	        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	  	        success : function(data) {
	  	        	treeSet();
	  	        	treeView(data);
	  	          	alert("삭제 되었습니다.");
	  	        },
	  	        complete : function(data) {
	  	              // 통신이 실패했어도 완료가 되었을 때 이 함수를 타게 된다.
	  	              // TODO
	  	        },
	  	        error : function(xhr, status, error) {
	  	              alert("에러발생");
	  	        }
	  	    }); 
	  }
	  function vld_securityCheck(ii){
		  var list = $("input[name='vld_security']");
		  
		  var checkLength=0;
		  for(var i = 0; i < list.length; i++){
		        if(list[i].checked){ 
		        	checkLength++;
		        }
		    }
		  if(checkLength>1){
			  alert('허용갯수 초과');
			  vld_security[ii].checked=false;
		  }
	  }
	  function connectionCheck(ii){
		  var list = $("input[name='connection']");
		  
		  var checkLength=0;
		  for(var i = 0; i < list.length; i++){
		        if(list[i].checked){ 
		        	checkLength++;
		        }
		    }
		  if(checkLength<2){
			  alert('허용갯수 부족');
			  connection[ii].checked=true;
		  }
	  }
	  function htmlSelect(title, stat){//중복되는 Select
		  HtmlStr='';
		  if(title=="security"){
			security();		    	
		  }else if(title="vld0"){
			 vld(stat);
		  }
		  center();
		  right(title);
	  }
	  function security(){
		  for(var i=0; i<response[0].length; i++){
	        	HtmlStr+='<tr>';
	        	HtmlStr+='<td>';
	        		HtmlStr+='<input class="form-control ng-pristine ng-invalid ng-invalid-required" type=text id ='+response[0][i]+' name='+response[0][i]+' value="'+response[1][i]+'" >';
	        	HtmlStr+='</td>';
	        	HtmlStr+='<td><input type="button" value="저장" class="btn btn-default btn-w-xs" onclick="javascript:update('+response[0][i]+');">';
	        	HtmlStr+='<input type="button" value="삭제" class="btn btn-default btn-w-xs" onclick="javascript:delete1('+response[0][i]+');"></td>';
	        	HtmlStr+='</tr>';
		  }
		  HtmlStr+='<tr>';
          HtmlStr+='<td colspan=2>';
		  HtmlStr+='<input type="button" value="추가" class="btn btn-default btn-w-xs" onclick="javascript:add();">';
		  HtmlStr+='</td>';
		  HtmlStr+='</tr>';
	  }
	  function vld(stat){
		  if(stat=="read"){
			  var tab1='&nbsp;&nbsp;&nbsp;';
			  var tab2='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'; 
			  var check='';
		        	
			  	HtmlStr+='<tr><td><input type="button" value="수정" class="btn btn-default btn-w-xs" onclick="javascript:read();">';
			  	HtmlStr+='<input type="button" value="추가" class="btn btn-default btn-w-xs" onclick="javascript:vldInsertPageGo();"><input type="button" value="삭제" class="btn btn-default btn-w-xs" onclick="javascript:vldDelete();"></td></tr>';
			  	HtmlStr+='<tr><td>';
			  	HtmlStr+='&lt;?xml version="1.0" encoding="utf-8" ?&gt;<br>';
	        	HtmlStr+='&lt;vld&gt;<br>';
	        	HtmlStr+=tab1+'&lt;id&gt;'+response[0][0]+'&lt;/id&gt;<br>';
	        	HtmlStr+=tab1+'&lt;vendor&gt;'+response[1][0]+'&lt;/vendor&gt;<br>';
	        	HtmlStr+=tab1+'&lt;descriptor_version&gt;'+response[2][0]+'&lt;/descriptor_version&gt;<br>';
	        	HtmlStr+=tab1+'&lt;number_of_endpoints&gt;'+response[3][0]+'&lt;/number_of_endpoints&gt;<br>';
	        	HtmlStr+=tab1+'&lt;root_requirement&gt;'+response[4][0]+'&lt;/root_requirement&gt;<br>';
	        	HtmlStr+=tab1+'&lt;leaf_requirement&gt;'+response[5][0]+'&lt;/leaf_requirement&gt;<br>';
	        	HtmlStr+=tab1+'&lt;qos&gt;<br>';
	        	for(var i=0; i<response[6].length; i++){
	        		if(response[6][i]!='NULL')
	        		HtmlStr+=tab2+'&lt;item&gt;'+response[6][i]+'&lt;/item&gt;<br>';
	        	}
	        	HtmlStr+=tab1+'&lt;/qos&gt;<br>';
	        	HtmlStr+=tab1+'&lt;test_access&gt;'+response[7][0]+'&lt;/test_access&gt;<br>';
	        	HtmlStr+=tab1+'&lt;connection&gt;<br>';
	        	for(var i=0; i<response[8].length; i++){
	        		if(response[8][i]!='NULL')
	        		HtmlStr+=tab2+'&lt;ref&gt;'+response[8][i]+'&lt;/ref&gt;<br>';
	        	}
	        	HtmlStr+=tab1+'&lt;/connection&gt;<br>';
	        	HtmlStr+=tab1+'&lt;connectivity_type&gt;'+response[9][0]+'&lt;/connectivity_type&gt;<br>';
	        	for(var i=0; i<response[10].length; i++){
	        		if(response[10][i]!='NULL')
	        		HtmlStr+=tab2+'&lt;vld_security&gt;'+response[10][i]+'&lt;/vld_security&gt;<br>';
	        	}
	        	HtmlStr+='&lt;/vld&gt;<br>';
	        	HtmlStr+='</td></tr>';
			  }else{
				  	if(response[0][0]!=null)
				  		HtmlStr+='<tr align=right><td colspan=2><input type="button" class="btn btn-default btn-w-xs" value="저장" onclick="javascript:xmlUpdate();"></td></tr>';
				  	else
					  HtmlStr+='<tr align=right><td colspan=2><input type="button" class="btn btn-default btn-w-xs" value="저장" onclick="javascript:xmlSave();"></td></tr>';
				  	HtmlStr+='<tr>';
				  	if(response[0][0]!=null)
		        		HtmlStr+='<td>id</td><td>'+response[0][0]+'</td>';
	        		else
	        			HtmlStr+='<td>id</td><td><input type=text class="form-control ng-pristine ng-invalid ng-invalid-required" id=vld_id name=vld_id ></td>';
		        	HtmlStr+='</tr>';
		        	HtmlStr+='<tr>';
		        	if(response[0][0]!=null)
		        		HtmlStr+='<td>vendor</td><td>'+response[1][0]+'</td>';
		        	else
	        			HtmlStr+='<td>vendor</td><td><input type=text class="form-control ng-pristine ng-invalid ng-invalid-required" id=vendor name=vendor ></td>';
		        	HtmlStr+='</tr>';
		        	HtmlStr+='<tr>';
		        	if(response[0][0]!=null)
		        		HtmlStr+='<td>descriptor_version</td><td>'+response[2][0]+'</td>';
		        	else
	        			HtmlStr+='<td>descriptor_version</td><td><input type=text class="form-control ng-pristine ng-invalid ng-invalid-required" id=descriptor_version name=descriptor_version ></td>';
		        	HtmlStr+='</tr>';
		        	HtmlStr+='<tr>';
		        	if(response[0][0]!=null)
		        		HtmlStr+='<td>number_of_endpoints</td><td><input type="text" class="form-control ng-pristine ng-invalid ng-invalid-required" id=number_of_endpoints name=number_of_endpoints value='+response[3][0]+' ></td>';
		        	else
		        		HtmlStr+='<td>number_of_endpoints</td><td><input type="text" class="form-control ng-pristine ng-invalid ng-invalid-required" id=number_of_endpoints name=number_of_endpoints  ></td>';
		        	HtmlStr+='</tr>';
		        	HtmlStr+='<tr>';
		        	if(response[0][0]!=null)
		        		HtmlStr+='<td>root_requirement</td><td><input type="text" class="form-control ng-pristine ng-invalid ng-invalid-required" id=root_requirement name=root_requirement value='+response[4][0]+' ></td>';
		        	else
		        		HtmlStr+='<td>root_requirement</td><td><input type="text" class="form-control ng-pristine ng-invalid ng-invalid-required" id=root_requirement name=root_requirement  ></td>';
		        	HtmlStr+='</tr>';
		        	HtmlStr+='<tr>';
		        	if(response[0][0]!=null)
		        		HtmlStr+='<td>leaf_requirement</td><td><input type="text" class="form-control ng-pristine ng-invalid ng-invalid-required" id=leaf_requirement name=leaf_requirement value='+response[5][0]+' ></td>';
		        	else
		        		HtmlStr+='<td>leaf_requirement</td><td><input type="text" class="form-control ng-pristine ng-invalid ng-invalid-required" id=leaf_requirement name=leaf_requirement  ></td>';
		        	HtmlStr+='</tr>';
		        	HtmlStr+='<tr border=1>';
		        	HtmlStr+='<td>qos</td><td>';
		        	for(var i=0; i<response[11].length; i++){
		        		for(var ii=0; ii<response[6].length; ii++)
		        			if(response[12][i]==response[6][ii]){
		        				check='checked';
		        			}
		        		HtmlStr+='<input id=qos name=qos type=checkbox '+check+' value='+response[11][i]+'>'+response[12][i]+'<br>';
		        		check='';
		        	}
		        	HtmlStr+='</td>';
		        	HtmlStr+='</tr>';
		        	HtmlStr+='<tr>';
		        	if(response[0][0]!=null)
		        		HtmlStr+='<td>test_access</td><td><input type="text" class="form-control ng-pristine ng-invalid ng-invalid-required" id=test_access name=test_access value='+response[7][0]+' ></td>';
		        	else
		        		HtmlStr+='<td>test_access</td><td><input type="text" class="form-control ng-pristine ng-invalid ng-invalid-required" id=test_access name=test_access  ></td>';
		        	HtmlStr+='</tr>';
		        	HtmlStr+='<tr>';
		        	HtmlStr+='<td>connection</td><td>';
		        	for(var i=0; i<response[13].length; i++){
		        		for(var ii=0; ii<response[8].length; ii++)
		        			if(response[14][i]==response[8][ii]){
		        				check='checked';
		        			}
		        		HtmlStr+='<input id=connection name=connection onclick="javascript:connectionCheck('+i+');" type=checkbox '+check+' value='+response[13][i]+'>'+response[14][i]+'<br>';
		        		check='';
		        	}
		        	HtmlStr+='</td>';
		        	HtmlStr+='</tr>';
		        	HtmlStr+='<tr>';
		        	if(response[0][0]!=null)
		        		HtmlStr+='<td>connectivity_type</td><td><input type="text" class="form-control ng-pristine ng-invalid ng-invalid-required" id=connectivity_type name=connectivity_type value='+response[9][0]+' ></td>';
		        	else
		        		HtmlStr+='<td>connectivity_type</td><td><input type="text" class="form-control ng-pristine ng-invalid ng-invalid-required" id=connectivity_type name=connectivity_type  ></td>';
		        	HtmlStr+='</tr>';
		        	HtmlStr+='<tr>';
		        	HtmlStr+='<td>vld_security</td><td>';
		        	if(response[10][0]!='NULL'&&response[0][0]!=null)
		        		HtmlStr+='<input id=vld_security name=vld_security type=radio value="N">NONE<br>';
		        	else
		        		HtmlStr+='<input id=vld_security name=vld_security type=radio checked value="N">NONE<br>';
		        	for(var i=0; i<response[15].length; i++){
		        		for(var ii=0; ii<response[10].length; ii++)
		        			if(response[16][i]==response[10][ii]){
		        				check='checked';
		        			}
		        		HtmlStr+='<input id=vld_security name=vld_security type=radio '+check+' value='+response[15][i]+'>'+response[16][i]+'<br>';
		        		check='';
		        	}
		        	HtmlStr+='</td>';
		        	HtmlStr+='</tr>';
			  }
			  	qos_ref_no=response[17][0];
			 	connection_ref_no=response[18][0];
				vld_security_ref_no=response[19][0];
				vld_no=response[20][0];
				version_no=response[21][0];
	  }
	  function center(){
		  var chartParent = $('#createInput').parent();
	      $('#createInput').remove();
	      $('<table id=createInput class="table">'+HtmlStr+'</table>').appendTo(chartParent);
	  }
	  function right(title){
		  var str2='';
	      str2="<img src='images/"+title+".png' alt='''>";
	      var chartParent2 = $('#right').parent();
	      $('#right').remove();
	      $('<div id=right>'+str2+'</div>').appendTo(chartParent2);
	  }
	</script>
	
	
	
		

	<div class="row"> 

		<!-- List -->
		<div class="col-md-4 col-lg-3">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="row">
						<div class="col-md-6 text-left">
							<span class="glyphicon glyphicon-th"></span>
							Descriptor List
						</div><!-- // col -->

						<div class="col-md-6 text-right">
							<!-- Sample Button - rochas 15.10.07 -->
								<div  class="btn-group" dropdown>
									<button type="button" ng-disabled="image == null"
										class="btn btn-info btn-sm dropdown-toggle">
										<span class="fa fa-plus"></span> add
									</button>
								</div>
							</div><!-- // col -->
					</div><!-- // row -->
				</div><!-- // panel-heading -->

				<div class="panel-body" style=height:494px>

					<div id="tree" > </div>
					
				</div><!-- panel -->
			</div><!-- // panel-body -->
		</div><!-- // List -->
		
		<!-- Detail -->
		<div class="col-md-8 col-lg-9">

			<div class="block" style="width:49%"><div class="box">
				<div class="panel panel-default" >
					<div class="panel-heading">
						<div class="row">
							<div class="col-md-6 text-left">
								<span class="glyphicon glyphicon-th"></span>
								Descriptor Viewer
							</div><!-- // col -->
				
							<div ng-show="system" class="col-md-6 text-right">
							</div><!-- // col -->
						</div><!-- // row -->
					</div><!-- // header -->
				
					<div class="panel-body" style=height:494px>
					<div class="custom-pre-scrollable" >
							<table id="createInput" class="table">
							</table>
							<table id="createInput2" class="table">
							</table>
						</div>
					</div><!-- // panel-body -->
				</div><!-- // panel -->
			</div></div>
			
			<div class="block" style="width:3%"><div class="box">
			&nbsp;
			</div></div>
			
			<div class="block" style="width:48%"><div class="box">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="row">
							<div class="col-md-6 text-left">
								<span class="glyphicon glyphicon-th"></span>
								Descriptor Document
							</div><!-- // col -->
				
							<div ng-show="system" class="col-md-6 text-right">
							</div><!-- // col -->
						</div><!-- // row -->
					</div><!-- // header -->
				
					<div class="panel-body" style=height:494px>
					<div id="right">
						
					</div><!-- // panel-body -->
				</div><!-- // panel -->
			</div></div>

		</div><!-- // Detail -->
	</div>

</div><!-- // SystemCtrl -->