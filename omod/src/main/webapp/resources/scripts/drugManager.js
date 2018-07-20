Ext.namespace('TRACCombo');

//var patientIdParameter = <c:out value="${patientId}"/>
/**
TRACCombo.programOptions = [<c:forEach var="programOption" items="${programOptions}">
	["<c:out value="${programOption.key}"/>","<c:out value="${programOption.value}"/>"],
</c:forEach>
];

TRACCombo.reasonStoppedOptions = [<c:forEach var="reasonStoppedOption" items="${reasonStoppedOptions}">
	["<c:out value="${reasonStoppedOption.key}"/>","<c:out value="${reasonStoppedOption.value}"/>"],
</c:forEach>
];

TRACCombo.drugOptions = [
<c:forEach var="drugOption" items="${drugOptions}">
	["<c:out value="${drugOption.key}"/>","<c:out value="${drugOption.value}"/>"],
</c:forEach>
];
**/
Ext.onReady(function() {

	/**********************************************
	 * The drug combo box
	 **********************************************/
    TRACCombo.drugComboBox = new Ext.form.ComboBox({
        x:60,
        y:5,
        displayField:'drugName',
       	valueField:'drugIndex',
        name:'drugId',
        hiddenName:'drugId',
        store:  new Ext.data.SimpleStore({
            id:'drugIndex',
            fields:['drugIndex', 'drugName']
                    ,data:TRACCombo.drugOptions
                }),
        
        typeAhead: true,
        mode: 'local',
        forceSelection: true,
        triggerAction: 'all',
        emptyText:'Select a drug...',
        selectOnFocus:true
    });




    /**********************************************************************************
     * The program combo box.
     * Its actually just a choice of concepts for the drug order
     **********************************************************************************/
    TRACCombo.programComboBox= new Ext.form.ComboBox({
        x:60,
        y:35,
        displayField:'programName',
       	valueField:'programIndex',
        name:'concept',
        hiddenName:'concept',
        store:  new Ext.data.SimpleStore({
            id: 'programIndex',
            fields:['programIndex', 'programName']
                    ,data:TRACCombo.programOptions
                }),
        typeAhead: true,
        mode: 'local',
        forceSelection: true,
        triggerAction: 'all',
        emptyText:'Select a program',
        selectOnFocus:true
    });

    /******************************************************
     * Reason drug order stopped
     *********************************************************/
    TRACCombo.reasonStoppedComboBox = new Ext.form.ComboBox({
        x:	80,
        y:	5,
        displayField:'reasonStopped',
       	valueField:'reasonId',
        name:'reasonId',
        hiddenName:'reasonId',
        store:  new Ext.data.SimpleStore({
            id:'reasonId',
            fields:['reasonId', 'reasonStopped']
                    ,data:TRACCombo.reasonStoppedOptions
                }),
        
        typeAhead: true,
        mode: 'local',
        forceSelection: true,
        triggerAction: 'all',
        emptyText:'Select Reason Stopped...',
        selectOnFocus:true
    });
    
    
	/*************************************
	 * The main drug form 
	 *************************************/
    var form = new Ext.form.FormPanel({
        id:'drug-form',
        baseCls: 'x-plain',
        layout:'absolute',
        url:'/openmrs/module/tracplus/savedrugs.form',
        baseParams: {patientId: patientIdParameter},
        defaultType: 'textfield',
		
        items: [{
            x: 0,
            y: 5,
            xtype:'label',
            text: 'Drug:'
        },
        	TRACCombo.drugComboBox,

		{	x: 0,
            y: 35,
            xtype:'label',
            text: 'Program:'
        },
    		TRACCombo.programComboBox,

    	{
            x: 0,
            y: 95,
            xtype:'label',
            text: 'Dose:'
        },{
            x: 60,
            y: 90,
            name: 'dose',
            width:150  // dose
        },{
            x: 0,
            y: 125,
            xtype:'label',
            text: 'Units:'
        },{
            x: 60,
            y: 120,
            name: 'units',
            width:150  // units
        },{
            x: 0,
            y: 155,
            xtype:'label',
            text: 'Frequency:',
        },{
            x: 60,
            y: 150,
            name: 'frequency',
            width:150  // frequency
        },{
            x: 0,
            y: 185,
            xtype: 'label',
            text: 'Start Date',  // start date
        },{
            x: 60,
            y: 180,
			xtype: 'datefield',
			format: 'd/m/Y',
            name: 'startdate',
            width:150  // start date
        },{
            x: 0,
            y: 215,
            xtype: 'label',
            text: 'Stop Date' , // stop date
            
        },{
            x: 60,
            y: 210,
			xtype: 'datefield',
			format: 'd/m/Y',
            name: 'stopdate',
            width:150  // stop date                
        },{
            x: 0,
            y: 245,
            xtype:'label',
            text: 'instructions:'
        },{            
            x:60,
            y: 240,
            xtype: 'textarea',
            name: 'instructions',
            anchor: '100%'  	// instructions 
        },{            
            //x:60,
            //y: 180,
            name: 'drugOrderId',
            hidden: true  	// instructions 
        }

        ]
    });
    
    var stopForm = new Ext.form.FormPanel({
        id:'stop-form',
        baseCls: 'x-plain',
        layout:'absolute',
        url:'/openmrs/module/tracplus/savedrugs.form',
        baseParams: {patientId: patientIdParameter},
        defaultType: 'textfield',
		
        items: 
        	[
    	 {
			x: 0,
			y: 5,
			xtype: 'label',
			text: 'Stop Reason' , // stop date
    	 },
            TRACCombo.reasonStoppedComboBox,
         {
            x: 0,
            y: 35,
            xtype: 'label',
            text: 'Stop Date' , // stop date
        },{
            x: 80,
            y: 35,
			xtype: 'datefield',
			format: 'd/m/Y',
            name: 'stopdate',
            width:150  // stop date                
        },{            
            //x:60,
            //y: 180,
            name: 'drugOrderId',
            hidden: true  	// instructions 
        }

        ]
    });
    
    var stopWindow = new Ext.Window({
        title: 			'Stop Order',
        width: 			350,
        height:			150,
        minWidth: 		300,
        minHeight: 		200,
        layout: 		'fit',
        plain:			true,
        bodyStyle:		'padding:5px;',
        buttonAlign:	'center',
        closeAction: 	'hide',
        items: 			stopForm,

        buttons: [{
            text: 'Submit',
            handler: function(){
            	Ext.getCmp("stop-form").getForm().submit({success:saveDrugSuccess});
            	stopWindow.hide();
            }
        },{
            text: 'Cancel',
            handler: function(){
        		stopWindow.hide();
        	}
        }]
    });

    var window = new Ext.Window({
        title: 'Drug Order',
        width: 500,
        height:400,
        minWidth: 300,
        minHeight: 200,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        closeAction: 'hide',
        items: form,

        buttons: [{
            text: 'Submit',
            handler: function(){
            	Ext.getCmp("drug-form").getForm().submit({success:saveDrugSuccess});
            	window.hide();
            }
        },{
            text: 'Cancel',
            handler: function(){
            	window.hide();
        	}
        }]
    });

    
	// success function
	var saveDrugSuccess = function(form,action){
							groupingStore.reload();
							}

 // create the Data Store
    var store = new Ext.data.JsonStore({
        root: 'drugs',
        totalProperty: 'totalCount',
        id: 'drugOrderId',
        //remoteSort: true,

        fields: [
                 {name:'drugId'},
            'drugOrderId','drugname', 
            'dose', 'dailydose', 'units', 
            'frequency',{name:'startdate',type:'date',dateFormat:'d/m/Y'},
            {name:'stopdate',type:'date',dateFormat:'d/m/Y'},'instructions','concept','conceptName'
        ],

        // load using script tags for cross domain, if the data in on the same domain as
        // this page, an HttpProxy would be better
        proxy: new Ext.data.HttpProxy({
            url: '/openmrs/module/tracplus/drugs.form'
        })
    });
    store.setDefaultSort('startdate', 'desc');
	store.baseParams = {patientId:patientIdParameter};

 	var groupingStore =  new Ext.data.GroupingStore({
		groupField:'conceptName',
		reader: new Ext.data.JsonReader({
			root: 'drugs',
			totalProperty: 'totalCount',
			id: 'drugOrderId'},
		 	[
				{name:'drugId'},'drugOrderId','drugname', 'dose', 'dailydose', 'units', 'reasonId',
	            'frequency',{name:'startdate',type:'date',dateFormat:'d/m/Y'},
	            {name:'stopdate',type:'date',dateFormat:'d/m/Y'},'instructions',{name:'concept'},'conceptName'
            ]),
		
		proxy: new Ext.data.HttpProxy({
			url: '/openmrs/module/tracplus/drugs.form'
		})
	});
 	groupingStore.setDefaultSort('startdate', 'desc');
 	groupingStore.baseParams = {patientId:patientIdParameter};

	// jsonreader


	
    // pluggable renders
    var myDateRenderer = Ext.util.Format.dateRenderer('d/m/Y');
    
    function renderLast(value, p, r){
        return String.format('{0}<br/>by {1}', value.dateFormat('M j, Y, g:i a'), r.data['lastposter']);
    }

    var pagingBar = new Ext.PagingToolbar({
        pageSize: 25,
        store: groupingStore,
        displayInfo: true,
        displayMsg: 'Displaying topics {0} - {1} of {2}',
        emptyMsg: "No topics to display",
        
        items:[
            '-', {
            pressed: false,
            enableToggle:true,
            text: '+',
            cls: 'x-btn-text-icon details',
            toggleHandler: function(btn, pressed){
                var view = grid.getView();
                view.showPreview = pressed;
                view.refresh();
            	}
     		},
     		{
             pressed: false,
             enableToggle:false,
             text: 'Edit',
             cls: 'x-btn-text-icon details',
             handler: function(btn, pressed){
     			var view = grid.getView();
     			window.show();
				view.refresh();
				
             	}
      		},
      		{
              pressed: false,
              enableToggle:false,
              text: 'Add',
              cls: 'x-btn-text-icon details',
              handler: function(btn, pressed){
      			Ext.getCmp("drug-form").getForm().reset();
      			window.show();
              }
       		},
       		{
            pressed: false,
            enableToggle:false,
            text: 'Stop',
            cls: 'x-btn-text-icon details',
            handler: function(btn, pressed){
       			var view = grid.getView();
       			Ext.getCmp("drug-form").getForm().reset();
    			stopWindow.show();
//    			/view.refresh();
            }
     		}
            ]
    });



    var grid = new Ext.grid.GridPanel({
        el:'container',
        width:700,
        height:250,
        //title:'Drugs',
        store: groupingStore,
        trackMouseOver:false,
        disableSelection:false,
        loadMask: true,
        sm: new Ext.grid.RowSelectionModel({
            singleSelect: true,
            listeners: {
                rowselect: function(sm, row, rec) {
	        		Ext.getCmp("drug-form").getForm().loadRecord(rec);
	        		Ext.getCmp("stop-form").getForm().loadRecord(rec);
                }
            }
        }),
        // grid columns
        columns:[
		{
		    id: 'drugId', // id assigned so we can apply custom css (e.g. .x-grid-col-topic b { color:#333 })
		    header: "Drug",
		    dataIndex: 'drugId',
		    width: 70,
		    hidden: true,
		    //renderer: renderTopic,
		    sortable: true
		},{
            //id: 'drug', // id assigned so we can apply custom css (e.g. .x-grid-col-topic b { color:#333 })
            header: "Drug",
            dataIndex: 'drugname',
            width: 70,
            //renderer: renderTopic,
            sortable: true
        },{
            header: "Dose",
            dataIndex: 'dose',
            width: 70,
            hidden: false,
            sortable: true
        },{
             header: "Order Id",
             dataIndex: 'drugOrderId',
             width: 35,
             hidden: true,
             sortable: true
        },{
            header: "Units",
            dataIndex: 'units',
            width: 70,
            align: 'right',
            sortable: true
        },{
            header: "Frequency",
            dataIndex: 'frequency',
            width: 70,
            //renderer: renderLast,
            sortable: true
        },{
            header: "Start Date",
            dataIndex: 'startdate',
            width: 70,
            renderer: myDateRenderer,
            sortable: true
        },{
	        header: "Stop Date",
	        dataIndex: 'stopdate',
	        width: 70,
	        renderer: myDateRenderer,
	        sortable: true
    	},{
        	id:'conceptName',
	        header: "Treatment",
	        dataIndex: 'conceptName',
	        width: 70,
	        sortable: true,
	        hidden:true
    	}

    	],

    	view: new Ext.grid.GroupingView({
            forceFit:true,
            groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "Items" : "Item"]})',
            enableRowBody:true,
            showPreview:false,
            getRowClass : function(record, rowIndex, p, store){
                if(this.showPreview){
                    p.body = '<p>'+record.data.instructions+'</p>';
                    return 'x-grid3-row-expanded';
                }
                return 'x-grid3-row-collapsed';
            }
        }),
        
        // customize view config
        /**viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:false,
            getRowClass : function(record, rowIndex, p, store){
                if(this.showPreview){
                    p.body = '<p>'+record.data.instructions+'</p>';
                    return 'x-grid3-row-expanded';
                }
                return 'x-grid3-row-collapsed';
            }
        },
**/
        // paging bar on the bottom
      bbar: pagingBar
    });

    // render it
    grid.render();

    // trigger the data store load
    groupingStore.load({params:{start:0, limit:25}});
});
