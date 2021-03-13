$( document ).ready(function() {
    console.log( "ready!" );
    
//    getServerList();
    
    getServerStatInfo(1);
    
//    setChart();
    
});

function getServerList(){
	$.ajax({
        url:'/pim/v1/servers',
        success:function(data){
            console.log(data);
            serverList = data;
            
            for(var idx in data){
            	getServerStatInfo(data[idx].serverId)
            }
        }
	});
}

function getServerStatInfo(serverNo){
	
	var that = this;
	
	$.ajax({
        url:'/pim/v1/servers/static/' + serverNo,
        success:function(data){
        	
        	that.setData(data);
        	
        }
	});
}

var dateList=[];
var cpuUsage ={};
var cpuUsageList =[];
var memUsage = [];
var eth2Rx = [];
var eth2Tx = [];
function setData(data){
	console.log(data);
	
	for(var i=0; i<data.length; i++){
		
    	var temp = data[i];
    	dateList.push(temp.statTime);
    	
    	for(var idx in temp.cpuUsageAvg){
    		if (cpuUsage[idx] == null){
    			cpuUsage[idx] = [];
    		}
    		cpuUsage[idx].push(temp.cpuUsageAvg[idx]);
    	}
    	
    	memUsage.push(temp.memUsageAvg)
    	
    	eth2Rx.push(parseInt(temp.nicUsage.eth2.split("/")[0]) || 0);
    	eth2Tx.push(parseInt(temp.nicUsage.eth2.split("/")[1]) || 0);
    }
	
	for(var idx in cpuUsage){
		var temp = {
			name : idx,
			data : cpuUsage[idx],
			type : "bar"
		}
		
		cpuUsageList.push(temp);
	}
	
	setCpuChart();
	setMemChart();
	setNicChart();
}

function setNicChart(){
	var myChart = echarts.init($('#nicView')[0]);
	
	var option = {
		    title : {
		        text: 'Nic eth2 사용량',
		        subtext: '5분 단위 사용량'
		    },
		    tooltip : {
		        trigger: 'axis'
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            magicType : {show: true, type: ['line', 'bar']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    xAxis : [
		        {
		            type : 'category',
		            data : dateList
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : [
		    	{
		    		name : "RX",
		    		data : eth2Rx,
		    		type : "bar"
		    	},
		    	{
		    		name : "TX",
		    		data : eth2Tx,
		    		type : "bar"
		    	}
		    ]
		};	
	
	myChart.setOption(option);
}

function setMemChart(){
	var myChart = echarts.init($('#memView')[0]);
	
	var option = {
		    title : {
		        text: 'Mem 사용량',
		        subtext: '5분 단위 사용량'
		    },
		    tooltip : {
		        trigger: 'axis'
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            magicType : {show: true, type: ['line', 'bar']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    xAxis : [
		        {
		            type : 'category',
		            data : dateList
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : {
				name : "avg",
				data : memUsage,
				type : "bar"
			}
		    
		};	
	
	myChart.setOption(option);
}

function setCpuChart(){
	var myChart = echarts.init($('#cpuView')[0]);
	
	var option = {
		    title : {
		        text: 'CPU 사용량',
		        subtext: '5분 단위 사용량'
		    },
		    tooltip : {
		        trigger: 'axis'
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            magicType : {show: true, type: ['line', 'bar']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    xAxis : [
		        {
		            type : 'category',
		            data : dateList
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : cpuUsageList
		    
		};	
	
	myChart.setOption(option);
}