
'use strict';
angular.module("app.descriptors")
.controller('ActiveViewDiagramCtrl', ["$scope", "logger", "$q", "$timeout", "$modal", "DescriptorTreeViewAPI", 
"OptionTreeViewAPI", "VLDatviewAPI", "VMatviewAPI", "DescriptorXml", "DescriptorAPI", 
function($scope, logger, $q, $timeout, $modal, DescriptorTreeViewAPI, 
OptionTreeViewAPI, VLDatviewAPI, VMatviewAPI, DescriptorXml, DescriptorAPI) {
    
    $scope.diagram;
    $scope.activeViewIsLoading = false;
    var $go = go.GraphObject.make;
    // define the Node templates for regular nodes
    var textColor = 'whitesmoke';
    var defaultFontStyle = "bold 11pt Helvetica, Arial, sans-serif";
    var cpColor = "#79C900";
    var cpFontStyle = "bold 10pt sans-serif";
    
    
    // helper definitions for node templates
    
    function nodeStyle() {
        return [
        // The Node.location comes from the "loc" property of the node data,
        // converted by the Point.parse static method.
        // If the Node.location is changed, it updates the "loc" property of the node data,
        // converting back using the Point.stringify static method.
        new go.Binding("location","loc",go.Point.parse).makeTwoWay(go.Point.stringify), 
        {
            // the Node.location is at the center of each node
            locationSpot: go.Spot.Center,
            //isShadowed: true,
            //shadowColor: "#888",
            // handle mouse enter/leave events to show/hide the ports
            mouseEnter: function(e, obj) {
                showPorts(obj.part, true);
            },
            mouseLeave: function(e, obj) {
                showPorts(obj.part, false);
            }
        }
        ];
    }
    
    // Define a function for creating a "port" that is normally transparent.
    // The "name" is used as the GraphObject.portId, the "spot" is used to control how links connect
    // and where the port is positioned on the node, and the boolean "output" and "input" arguments
    // control whether the user can draw links from or to the port.
    function makePort(name, spot, output, input) {
        // the port is basically just a small circle that has a white stroke when it is made visible
        return $go(go.Shape, "Circle", 
        {
            fill: "transparent",
            stroke: null ,
            // this is changed to "white" in the showPorts function
            desiredSize: new go.Size(8,8),
            alignment: spot,
            alignmentFocus: spot,
            // align the port on the main Shape
            portId: name,
            // declare this object to be a "port"
            fromSpot: spot,
            toSpot: spot,
            // declare where links may connect at this port
            fromLinkable: output,
            toLinkable: input,
            // declare whether the user may draw links to/from here
            cursor: "pointer"// show a different cursor to indicate potential link point
        });
    }
    // Define a function for creating a "port" that is normally transparent.
    // The "name" is used as the GraphObject.portId, the "spot" is used to control how links connect
    // and where the port is positioned on the node, and the boolean "output" and "input" arguments
    // control whether the user can draw links from or to the port.
    function makeCP(name, spot, output, input) {
        // the port is basically just a small circle that has a white stroke when it is made visible
        
        var panel = $go(go.Panel, "Auto", 
        $go(go.Shape, "Circle", 
        {
            fill: cpColor,
            stroke: null ,
            // this is changed to "white" in the showPorts function
            desiredSize: new go.Size(40,40),
            alignment: spot,
            alignmentFocus: spot,
            // align the port on the main Shape
            portId: name,
            // declare this object to be a "port"
            fromSpot: spot,
            toSpot: spot,
            // declare where links may connect at this port
            fromLinkable: output,
            toLinkable: input,
            // declare whether the user may draw links to/from here
            cursor: "pointer"// show a different cursor to indicate potential link point
        }), 
        $go(go.TextBlock, name, // the name of the port
        {
            font: cpFontStyle,
            margin: new go.Margin(0,0,0,0)
        }));
        
        
        return panel;
    }
    function makeCPPort(cp, spot) {
        // the port is basically just a small circle that has a white stroke when it is made visible
        
        var panel = $go(go.Panel, "Auto", 
        $go(go.Shape, "Circle", 
        {
            fill: cpColor,
            stroke: null ,
            // this is changed to "white" in the showPorts function
            desiredSize: new go.Size(40,40),
            alignment: spot,
            alignmentFocus: spot,
            // align the port on the main Shape
            portId: cp.cp_no + "",
            // declare this object to be a "port"
            fromSpot: spot,
            toSpot: spot,
            // declare where links may connect at this port
            fromLinkable: true,
            toLinkable: true,
            // declare whether the user may draw links to/from here
            cursor: "pointer"// show a different cursor to indicate potential link point
        }), 
        $go(go.TextBlock, cp.cp_id, // the name of the port
        {
            font: cpFontStyle,
            stroke: textColor,
            margin: new go.Margin(0,0,0,0)
        }));
        
        
        return panel;
    }
    
    function makeTemplate(panelModel) {//category, cpList) {
        var topPorts = [];
        var bottomPorts = [];
        if (panelModel.ports.length == 1) {
            bottomPorts.push(makeCPPort(panelModel.ports[0], go.Spot.Bottom));
        } else if (panelModel.ports.length == 2 && panelModel.category.indexOf('151028030002') >= 0) {
            bottomPorts.push(makeCPPort(panelModel.ports[0], go.Spot.Bottom));
            topPorts.push(makeCPPort(panelModel.ports[1], go.Spot.Top));
        } else if (panelModel.ports.length == 2 && panelModel.category.indexOf('151028030002') < 0) {
            topPorts.push(makeCPPort(panelModel.ports[0], go.Spot.Top));
            topPorts.push(makeCPPort(panelModel.ports[1], go.Spot.Top));
        } else if (panelModel.ports.length == 3) {
            bottomPorts.push(makeCPPort(panelModel.ports[0], go.Spot.Bottom));
            bottomPorts.push(makeCPPort(panelModel.ports[1], go.Spot.Bottom));
            topPorts.push(makeCPPort(panelModel.ports[2], go.Spot.Top));
        } else if (panelModel.ports.length == 4) {
            bottomPorts.push(makeCPPort(panelModel.ports[0], go.Spot.Bottom));
            bottomPorts.push(makeCPPort(panelModel.ports[1], go.Spot.Bottom));
            topPorts.push(makeCPPort(panelModel.ports[2], go.Spot.Top));
            topPorts.push(makeCPPort(panelModel.ports[3], go.Spot.Top));
        }
        $scope.diagram.nodeTemplateMap.add(panelModel.category, // the default category
        $go(go.Node, "Spot", nodeStyle(), 
        // the main object is a Panel that surrounds a TextBlock with a rectangular Shape
        $go(go.Panel, "Auto", 
        $go(go.Shape, "RoundedRectangle", 
        {
            width: 150,
            height: 75
        }, 
        {
            fill: "#00A9C9",
            stroke: null 
        }, 
        new go.Binding("figure","figure")), 
        $go(go.TextBlock, 
        {
            font: defaultFontStyle,
            stroke: textColor,
            margin: 8,
            maxSize: new go.Size(160,NaN),
            wrap: go.TextBlock.WrapFit,
            editable: true
        }, 
        new go.Binding("text").makeTwoWay())
        ), 
        $go(go.Panel, "Horizontal", 
        {
            width: 82,
            height: 42
        }, 
        {
            alignment: go.Spot.Top,
            alignmentFocus: topPorts.length == 2 ? new go.Spot(0,0.5,-40,0) : new go.Spot(0,0.5,-20,0)/*1개일 때(0, 1, -20, 20), 2개일 때는 (0, 1, -40, 20)*/
        }, 
        topPorts), 
        $go(go.Panel, "Horizontal", 
        {
            width: 82,
            height: 42
        }, 
        {
            alignment: go.Spot.Bottom,
            alignmentFocus: bottomPorts.length == 2 ? new go.Spot(0,0.5,-40,0) : new go.Spot(0,0.5,-20,0)
        }, 
        bottomPorts)
        // four named ports, one on each side:
        
        ));
    }
    
    /*
     * VLD의 위치를 기준으로 TO VM의 위치가 자신의 폭 보다 더 왼쪽이면 Left, 오른쪽이면 Right의 Port를 추가한다.
     * 그 위치가 가운데이때 위쪽이면 Top, 아래쪽이면 Bottom을 추가한다.
     */
    function calcPortDirection(panelPosition, toPosition) {
        var vldWidth = 0;
        var vldHeight = 0;
        var vmWidth = 0;
        var vmHeight = 0;
        var marginLR = 120;
        var marginTB = 70;
        var position = 'Top';
        if (toPosition.left < 40) {
            position = 'Left';
        } else if ((panelPosition.left - (marginLR)) > toPosition.left) {
            position = 'Left';
        } else if ((panelPosition.left + (marginLR)) < toPosition.left) {
            position = 'Right';
        } else if ((panelPosition.top - (marginTB)) > toPosition.top) {
            position = 'Top';
        } else if ((panelPosition.top + (marginTB)) < toPosition.top) {
            position = 'Bottom';
        }
        return position;
    }
    
    
    // Define a function for creating a "port" that is normally transparent.
    // The "name" is used as the GraphObject.portId, the "spot" is used to control how links connect
    // and where the port is positioned on the node, and the boolean "output" and "input" arguments
    // control whether the user can draw links from or to the port.
    function makeVLDPort(port, portDirection) {
        // the port is basically just a small circle that has a white stroke when it is made visible
        var spot = go.Spot[portDirection];
        
        return $go(go.Shape, "Circle", 
        {
            fill: "transparent",
            stroke: null ,
            // this is changed to "white" in the showPorts function
            desiredSize: new go.Size(8,8),
            alignment: spot,
            alignmentFocus: spot,
            // align the port on the main Shape
            portId: port.toPort,
            // declare this object to be a "port"
            fromSpot: spot,
            toSpot: spot,
            // declare where links may connect at this port
            fromLinkable: true,
            toLinkable: true,
            // declare whether the user may draw links to/from here
            cursor: "pointer"// show a different cursor to indicate potential link point
        });
    }
    function makeVLDTemplate(panelModel) {
        var ports = [];
        /*
    	 * VLD의 위치를 기준으로 TO VM의 위치가 자신의 폭 보다 더 왼쪽이면 Left, 오른쪽이면 Right의 Port를 추가한다.
    	 * 그 위치가 가운데이때 위쪽이면 Top, 아래쪽이면 Bottom을 추가한다.
    	 * 이미 한 번 추가된 방향의 포트는 다시 추가하지 않고 쌓아놨다가 모든 cp_list를 처리한 후 T, L, R, B의 우선순위로 그냥 추가한다.
    	 * 4개가 초과되는 port들은 모두 각각의 cp_no를 id로 가지는 port로서 Center위치에 추가한다.
    	 */
        var panelPosition = {
            "left": panelModel.loc.split(' ')[0] - 0,
            "top": panelModel.loc.split(' ')[1] - 0,
            "loc": panelModel.loc
        }
        var existingPortDirections = [];
        for (var i = 0; i < panelModel.ports.length; i++) {
            var toId = $scope.portMap[panelModel.ports[i].cp_no].to;
            var loc = $scope.positions[toId];
            var portDirection = 'Center';
            if (loc != undefined) {
                var toPosition = {
                    "left": loc.split(' ')[0] - 0,
                    "top": loc.split(' ')[1] - 0,
                    "loc": loc
                };
                portDirection = calcPortDirection(panelPosition, toPosition);
                if (existingPortDirections.indexOf(portDirection) >= 0) {
                    /*
                     * T가 이미 추가되어 있으면 L, B가 이미 추가되어 있으면 R로 추가
                     * 이미 한 번 추가된 방향의 포트는 다시 추가하지 않고 쌓아놨다가 모든 cp_list를 처리한 후 T, L, R, B의 우선순위로 그냥 추가한다.
                     * 4개가 초과되는 port들은 모두 각각의 cp_no를 id로 가지는 port로서 Center위치에 추가한다.
                     */

                    if (portDirection == 'Top' && existingPortDirections.indexOf('Left') < 0) {
                        portDirection = 'Left';
                        existingPortDirections.push(portDirection);
                    } else if (portDirection == 'Bottom' && existingPortDirections.indexOf('Right') < 0) {
                        portDirection = 'Right';
                        existingPortDirections.push(portDirection);
                    } else {
                        portDirection = 'Center';
                    }

                } else {
                    existingPortDirections.push(portDirection);
                }
            }
            ports.push(makeVLDPort($scope.portMap[panelModel.ports[i].cp_no], portDirection));
            //name, spot, output, input)
        }
        
        $scope.diagram.nodeTemplateMap.add(panelModel.category, // the VLD category
        $go(go.Node, "Spot", nodeStyle(), 
        {
            width: 90,
            height: 50
        }, 
        // the main object is a Panel that surrounds a TextBlock with a rectangular Shape
        $go(go.Panel, "Auto", 
        $go(go.Shape, "Ellipse", 
        {
            fill: "#E69118",
            stroke: null 
        }, 
        new go.Binding("figure","figure")), 
        $go(go.TextBlock, 
        {
            font: defaultFontStyle,
            stroke: textColor,
            margin: 8,
            maxSize: new go.Size(160,NaN),
            wrap: go.TextBlock.WrapFit,
            editable: true
        }, 
        new go.Binding("text").makeTwoWay())
        ), 
        // four named ports, one on each side:
        ports
        ));
    
    }
    // Make link labels visible if coming out of a "conditional" node.
    // This listener is called by the "LinkDrawn" and "LinkRelinked" DiagramEvents.
    function showLinkLabel(e) {
        var label = e.subject.findObject("LABEL");
        if (label !== null )
            label.visible = (e.subject.fromNode.data.figure === "Ellipse");
    }
    
    // Make all ports on a node visible when the mouse is over the node
    function showPorts(node, show) {
        var diagram = node.diagram;
        if (!diagram || diagram.isReadOnly || !diagram.allowLink)
            return;
        node.ports.each(function(port) {
            port.stroke = (show ? "white" : null );
        }
        );
    }
    
    if (window.localStorage.getItem("ACTIVEVIEW_DIAGRAM_POSITIONS") == null) {
        window.localStorage.setItem("ACTIVEVIEW_DIAGRAM_POSITIONS", '{"150912070001": "0 295", "150912070002": "800 295", "150912070003": "400 0", "151028030001": "145 155", "151028030002": "670 155", "151028030003": "555 400", "151028030015": "798 400", "151028030004": "400 155", "151028030005": "245 400", "150912310001": "125.3 295", "151005310002": "400 255", "151005310003": "670 295", "151116310070": "820 295", "151005310004": "400 330", "151005310005": "400 60"}');
    }
//     $scope.positions = {
//         "150912070001": "0 295",        //cp01
//         "150912070002": "900 295",      //cp02
//         "150912070003": "400 0",        //cp03
//         "151028030001": "145 155",      //MME
//         "151028030002": "670 155",      //PGW-CP
//         "151028030003": "555 400",      //PGW-DP
//         "151028030015": "798 400",      //PGW-EX
//         "151028030004": "400 155",      //SGW-CP
//         "151028030005": "245 400",      //SGW-DP
//         "150912310001": "125.3 295",    //VLD01
//         "151005310002": "400 255",      //VLD02
//         "151005310003": "670 295",      //VLD03
//         "151116310070": "820 295",      //VLD70
//         "151005310004": "400 330",      //VLD04
//         "151005310005": "400 60"        //VLD05
//     };
    $scope.refreshData = function(testCase) {
        if ($scope.activeViewIsLoading == true) {
            return false;
        }
        $scope.activeViewIsLoading = true;
        
        $q.all([VLDatviewAPI.get().$promise, VMatviewAPI.get().$promise]).then(function(result) {
            //            console.log('result[0]', JSON.stringify(result[0].vld_list, null, 4));
            //            console.log('result[1] ', JSON.stringify(result[1].vm_list, null, 4));
            
            var nodeDataArray = [];
            var linkDataArray = [];
            $scope.positions = JSON.parse(window.localStorage.getItem("ACTIVEVIEW_DIAGRAM_POSITIONS"));
            var unknownPosition = false;
            $scope.portMap = {};
            $scope.nodeMap = {};
            $scope.cpNoList = [];

            if(testCase != undefined) {
                result[0].vld_list = JSON.parse(window.localStorage.getItem("vld_list_ex_" + testCase)).vld_list;
                result[1].vm_list = JSON.parse(window.localStorage.getItem("vm_list_ex_" + testCase)).vm_list;
            }

            for (var i = 0; i < result[0].vld_list.vld.length; i++) {
                var vld = result[0].vld_list.vld[i];
                if (vld.vld_no == 151116310062) {   //TODO: vld76 sample skip 릴리즈에서는 제거해야 함
                    continue;
                }
                var category = "VLD_" + vld.vld_no + "";
                var loc = $scope.positions[vld.vld_no + ""];
                if (loc == undefined) {
                    unknownPosition = true;
                    loc = "0 0";
                }
                var panelModel = {
                    "key": vld.vld_no + "",
                    "category": category,
                    "text": vld.vld_id,
                    "loc": loc
                };
                nodeDataArray.push(panelModel);
                
                $scope.nodeMap[vld.vld_no + ""] = panelModel;
                $scope.nodeMap[vld.vld_no + ""]["ports"] = vld.cp_list.cp;
                
                
                for (var j = 0; j < vld.cp_list.cp.length; j++) {
                    var cp = vld.cp_list.cp[j];
                    $scope.portMap[cp.cp_no + ""] = {
                        "from": vld.vld_no + "",
                        "fromPort": cp.cp_no + "",
                        "text": cp.cp_id
                    };
                    $scope.cpNoList.push(cp.cp_no + "");
                }
            }
            for (i = 0; i < result[1].vm_list.vm.length; i++) {
                var vm = result[1].vm_list.vm[i];
                var category = "VM_" + vm.vm_no + "";
                if (vm.cp_list.cp != undefined && vm.cp_list.cp.constructor != Array) {
                    vm.cp_list.cp = [vm.cp_list.cp];
                }
                var loc = $scope.positions[vm.vm_no + ""];
                if (loc == undefined) {
                    unknownPosition = true;
                    loc = "0 0";
                }
                var panelModel = {
                    "key": vm.vm_no + "",
                    "category": category,
                    "text": vm.vm_id,
                    "vm_state": vm.vm_state,
                    "loc": loc
                };
                nodeDataArray.push(panelModel);
                
                $scope.nodeMap[vm.vm_no + ""] = panelModel;
                $scope.nodeMap[vm.vm_no + ""]["ports"] = vm.cp_list.cp;

                //makeTemplate(category, vm.cp_list.cp);
                
                for (var j = 0; j < vm.cp_list.cp.length; j++) {
                    var cp = vm.cp_list.cp[j];
                    $scope.portMap[cp.cp_no + ""]["to"] = vm.vm_no + "";
                    $scope.portMap[cp.cp_no + ""]["toPort"] = cp.cp_no + "";
                    $scope.portMap[cp.cp_no + ""]["text"] = cp.cp_id;
                    if ($scope.cpNoList.indexOf(cp.cp_no + "") < 0) {
                        $scope.cpNoList.push(cp.cp_no + "");
                    }
                }
            }
            
            var cp01 = {
                "key": "150912070001",
                "category": "INOUT_PORT",
                "text": "cp01",
                "loc": $scope.positions["150912070001"],
                "ports": [{
                    "cp_no": "150912070001", "cp_id": "150912070001"
                }]
            };
            nodeDataArray.push(cp01);
            $scope.nodeMap["150912070001"] = cp01;
            $scope.portMap["150912070001"]["to"] = "150912070001";
            $scope.portMap["150912070001"]["toPort"] = "150912070001";


            var cp02 = {
                "key": "150912070002",
                "category": "INOUT_PORT",
                "text": "cp02",
                "loc": $scope.positions["150912070002"],
                "ports": [{
                    "cp_no": "150912070002", "cp_id": "150912070002"
                }]
            };
            nodeDataArray.push(cp02);
            $scope.nodeMap["150912070002"] = cp02;
            $scope.portMap["150912070002"]["to"] = "150912070002";
            $scope.portMap["150912070002"]["toPort"] = "150912070002";

            var cp03 = {
                "key": "150912070003",
                "category": "INOUT_PORT",
                "text": "cp03",
                "loc": $scope.positions["150912070003"],
                "ports": [{
                    "cp_no": "150912070003", "cp_id": "150912070003"
                }]
            };
            nodeDataArray.push(cp03);
            $scope.nodeMap["150912070003"] = cp03;
            $scope.portMap["150912070003"]["to"] = "150912070003";
            $scope.portMap["150912070003"]["toPort"] = "150912070003";

            for (i = 0; i < $scope.cpNoList.length; i++) {
                var cpNo = $scope.cpNoList[i];
                if ($scope.portMap[cpNo].to == undefined) {
                    
                    $scope.portMap[cpNo]["to"] = cpNo;
                    $scope.portMap[cpNo]["toPort"] = cpNo == "150912070001" ? "R" : (cpNo == "150912070002" ? "L" : "B");
                    $scope.portMap[cpNo]["text"] = $scope.portMap[cpNo].cp_id;
                }
                
                linkDataArray.push($scope.portMap[cpNo]);
            }
            
            //TODO: positions 위치 계산
            /*
            localStorage로부터 알고 있는 VLD와 vm의 position 정보를 가져온다.
            가져온 정보와 비교하여 새로운 요소가 있다면 아래의 알고리즘에 의해 전체 위치를 재배치한다.
            추가 변경된 내용이 없다면 localStorege로부터 가져온 정보를 토대로 그린다.

            재배치 알고리즘
            cp01을 시작점으로 cp02로 이어지는 위쪽 그룹과 cp03으로 이어지는 아래쪽 그룹으로 나뉘어진다.
            cp01은 항상 vld01과 이어지는 것으로 고정한다.
                각 그룹 내에서 vm의 수에 따라 자리를 배정 
                가로 폭이 계속 증가하며 vm 간의 가로 간격은 균등 분할하여 배치
                위쪽 그룹에서는 cp03과 이어지는 vld를 제외하고는 모든 vld는 vm 보다 아래쪽에 배치
                아래쪽 그룹에서는 위와 반대로 vld를 vm보다 위에 배치하고 
                vm의 cp_list 가 3개 이상인 경우 아래쪽에 port를 추가하고 이와 이어지는 vld는 Bottom port를 제공한다.
                cp01, cp02와 이어지는 vld 를 제외하고 Left, Right port 를 우선 제공(단 port가 3개 이상인 경우 Bottom 도 추가)
                cp01과 이어지는 vld 의 port 는 (cp01: L, T, B) 를 제공하고 
                cp02와 이어지는 vld 의 port 는 (cp02: B, R) 를 제공하고 필요에 따라 Left가 추가될 수 있다.
                cp01, cp02와 이어지는 vld 의 위치는 cp01과 cp02의 Top 값과 같은 Top 값 지정
                    이 vld와 이어지는 vm과 bottom으로 이어 질 수 있는 위치의 Left 값 지정
            */

            function countVM (fromID, toID) {
                console.log('vmList.length:', $scope.vmList.length, ', nodeMap[toID].category:', $scope.nodeMap[toID].category, ', fromID:', fromID, ', toID:', toID);
                
                if ($scope.nodeMap[toID].ports.length > 0) {
                    var subFromID = toID;
                    for (var i = 0; i < $scope.nodeMap[toID].ports.length; i++) {
                        cpNo = $scope.nodeMap[toID].ports[i].cp_no;
                        var subToID;
                        if ($scope.nodeMap[toID].category.indexOf("VLD_") >= 0) {
                            subToID = $scope.portMap[cpNo].from;
                        } else {
                            subToID = $scope.portMap[cpNo].to;
                        }
                        
                        if (subFromID != subToID) {
                            countVM (subFromID, subToID);
                        }
                    }
                    
                }
                if ($scope.nodeMap[toID].category.indexOf("VM_") >= 0) {
                    if ($scope.vmList.indexOf(toID) < 0) {
                        $scope.vmList.push(toID);
                    }
                }
            }
            if (unknownPosition) {
                console.log('위치를 알 수 없는 요소가 발견되어 있어 전체 위치 정보를 재조정합니다.');
                //cp02, cp03을 시작하여 각 요소의 개수를 구한다.
                $scope.vmList = [];
                var fromID;
                var toID = '150912070003'; //cp03
                countVM(fromID, toID);
                console.log('cp03을 시작하여 각 요소의 개수: ', $scope.vmList.length);
                for (i = 0; i < nodeDataArray.length; i++) {
                    var panelModel = nodeDataArray[i];
                    var key = panelModel.key;
                    if ($scope.nodeMap[key] != undefined) {
                        if (panelModel.category.indexOf('VLD') >= 0) {
                            //TODO: VLD 위치계산
                        } else {
                            //TODO: VM 위치계산
                        }
                    }
                }   
            }
            
            for (i = 0; i < nodeDataArray.length; i++) {
                var panelModel = nodeDataArray[i];
                var key = panelModel.key;
                if ($scope.nodeMap[key] != undefined) {
                    if (panelModel.category.indexOf('VLD') >= 0) {
                        makeVLDTemplate($scope.nodeMap[key]);
                    } else if (panelModel.category.indexOf('VM') >= 0) {
                        makeTemplate($scope.nodeMap[key]);
                    }
                }
            }
            
            var overviewData = {
                "class": "go.GraphLinksModel",
                "linkFromPortIdProperty": "fromPort",
                "linkToPortIdProperty": "toPort",
                "nodeDataArray": [],
                "linkDataArray": []
            };
            
            var model = go.Model.fromJson(overviewData);
            model.nodeDataArray = nodeDataArray;
            
            model.linkDataArray = linkDataArray;
            
            //            console.log(JSON.stringify(nodeDataArray, null, 4));
            //            console.log(JSON.stringify(linkDataArray, null, 4));
            
            $scope.diagram.model = model;
        
        }
        )["finally"](function() {
            console.log('activeViewLoading finally');
            $scope.activeViewIsLoading = false;
        }
        );
    }
    ;
    
    $scope.init = function() {
        console.log('ActiveViewDiagramCtrl init:');
        $scope.diagram = $go(go.Diagram, "diagramDescriptors", {
            "initialContentAlignment": go.Spot.Center,
            // Center
            // Diagram
            // Contents
            "allowDrop": true,
            // must be true to accept drops from the Palette
            "LinkDrawn": showLinkLabel,
            // this DiagramEvent listener is defined below
            "LinkRelinked": showLinkLabel,
            "animationManager.duration": 800,
            // slightly longer than default (600ms) animation
            "undoManager.isEnabled": true // enable undo & redo
        
        });
        
        
        // when the document is modified, add a "*" to the title and enable the "Save" button
        $scope.diagram.addDiagramListener("Modified", function(e) {
            var button = document.getElementById("SaveButton");
            if (button)
                button.disabled = !$scope.diagram.isModified;
            var idx = document.title.indexOf("*");
            if ($scope.diagram.isModified) {
                if (idx < 0)
                    document.title += "*";
            } else {
                if (idx >= 0)
                    document.title = document.title.substr(0, idx);
            }
        }
        );
        
        
        $scope.diagram.linkTemplate = 
        $go(go.Link, 
        {
            routing: go.Link.Orthogonal,
            corner: 5,
            relinkableFrom: true,
            relinkableTo: true
        }, 
        $go(go.Shape, {
            stroke: "gray",
            strokeWidth: 2
        }), 
        $go(go.Shape, {
            stroke: "gray",
            fill: "gray",
            toArrow: "Circle"
        })
        );
        
        $scope.diagram.nodeTemplateMap.add("", // the default category
        $go(go.Node, "Spot", nodeStyle(), 
        // the main object is a Panel that surrounds a TextBlock with a rectangular Shape
        $go(go.Panel, "Auto", 
        $go(go.Shape, "RoundedRectangle", 
        {
            width: 150,
            height: 75
        }, 
        {
            fill: "#00A9C9",
            stroke: null 
        }, 
        new go.Binding("figure","figure")), 
        $go(go.TextBlock, 
        {
            font: defaultFontStyle,
            stroke: textColor,
            margin: 8,
            maxSize: new go.Size(160,NaN),
            wrap: go.TextBlock.WrapFit,
            editable: true
        }, 
        new go.Binding("text").makeTwoWay())
        ), 
        $go(go.Panel, "Horizontal", 
        {
            width: 82,
            height: 42
        }, 
        {
            alignment: go.Spot.Top,
            alignmentFocus: new go.Spot(0,0.5,-40,0)/*1개일 때(0, 1, -20, 20), 2개일 때는 (0, 1, -40, 20)*/
        }, 
        [makeCP("T", go.Spot.Top, true, true), makeCP("L", go.Spot.Top, true, true)]), 
        $go(go.Panel, "Horizontal", 
        {
            width: 82,
            height: 42
        }, 
        {
            alignment: go.Spot.Bottom,
            alignmentFocus: new go.Spot(0,0.5,-40,0)
        }, 
        [makeCP("R", go.Spot.Bottom, true, true), makeCP("B", go.Spot.Bottom, true, true)])
        // four named ports, one on each side:
        
        ));
        
        $scope.diagram.nodeTemplateMap.add("INOUT_PORT", 
        $go(go.Node, "Spot", nodeStyle(), 
        $go(go.Panel, "Auto", 
        $go(go.Shape, "Circle", 
        {
            minSize: new go.Size(40,40),
            fill: cpColor,
            stroke: null 
        }), 
        $go(go.TextBlock, "Start", 
        {
            font: cpFontStyle,
            stroke: textColor
        }, 
        new go.Binding("text"))
        )
        ));
        
        
        
        // replace the default Link template in the linkTemplateMap
        $scope.diagram.linkTemplate = 
        $go(go.Link, // the whole link panel
        {
            routing: go.Link.AvoidsNodes,
            curve: go.Link.JumpOver,
            corner: 5,
            relinkableFrom: true,
            relinkableTo: true,
            reshapable: true,
            resegmentable: true,
            toShortLength: 0,
            //화살표 머리를 제거하고 벌어진 간격을 메워준다.
            // mouse-overs subtly highlight links:
            mouseEnter: function(e, link) {
                link.findObject("HIGHLIGHT").stroke = "rgba(30,144,255,0.2)";
            },
            mouseLeave: function(e, link) {
                link.findObject("HIGHLIGHT").stroke = "transparent";
            }
        }, 
        new go.Binding("points").makeTwoWay(), 
        $go(go.Shape, // the highlight shape, normally transparent
        {
            isPanelMain: true,
            strokeWidth: 8,
            stroke: "transparent",
            name: "HIGHLIGHT"
        }), 
        $go(go.Shape, // the link path shape
        {
            isPanelMain: true,
            stroke: "gray",
            strokeWidth: 2
        }), 
        $go(go.Shape, // the arrowhead
        {
            toArrow: "Line",
            stroke: null ,
            fill: "transparent"
        })
        //              ,
        //            $go(go.Panel, "Auto",  // the link label, normally not visible
        //              { visible: false, name: "LABEL", segmentIndex: 2, segmentFraction: 0.5},
        //              new go.Binding("visible", "visible").makeTwoWay(),
        //              $go(go.Shape, "RoundedRectangle",  // the label shape
        //                { fill: "#F8F8F8", stroke: null }),
        //              $go(go.TextBlock, "Yes",  // the label
        //                {
        //                  textAlign: "center",
        //                  font: "10pt helvetica, arial, sans-serif",
        //                  stroke: "#333333",
        //                  editable: true
        //                },
        //                new go.Binding("text", "text").makeTwoWay())
        //            )
        );
        
        // temporary links used by LinkingTool and RelinkingTool are also orthogonal:
        $scope.diagram.toolManager.linkingTool.temporaryLink.routing = go.Link.Orthogonal;
        $scope.diagram.toolManager.relinkingTool.temporaryLink.routing = go.Link.Orthogonal;
        
        
        
        $scope.refreshData();
    
    };

}
]);
