if (typeof Effect == 'undefined') {
	throw("accordion.js requires including script.aculo.us' effects.js library!");
}
var accordion = Class.create();
accordion.prototype = {
	//
	//  Setup the Variables
	//
	showAccordion : null,
	currentAccordion : null,
	duration : null,
	effects : [],
	animating : false,
		
	//  
	//  Initialize the accordions
	//
	initialize: function(container, options) {
		if (!$(container)) {
			throw(container+" doesn't exist!");
			return false;
		}
		this.options = Object.extend({
			resizeSpeed : 9,
			classNames : {
				toggle : 'accordion_toggle',
				toggleActive : 'accordion_toggle_active',
				content : 'accordion_content'
			},
			defaultSize : {
				height : null,
				width : null
			},
			direction : 'vertical',
			onEvent : 'click'
		}, options || {});
		
		this.duration = ((11-this.options.resizeSpeed)*0.15);

		var accordions = $$('#'+container+' .'+this.options.classNames.toggle);
		accordions.each(function(accordion) {
			Event.observe(accordion, this.options.onEvent, this.selecciona.bind(this, accordion), false);
			if (this.options.onEvent == 'click') {
				accordion.onclick = function() {return false;};
			}
			if (this.options.direction == 'horizontal') {
				var options = $H({width: '0px'});
			} else {
				var options = $H({height: '0px'});			
			}
			options.merge({display: 'none'});			
			this.currentAccordion = $(accordion.next(0)).setStyle(options);			
		}.bind(this));
	},
	
	selecciona : function(accordion) {
		if (this.animating) {
			return false;
		}
		this.effects = [];
		this.currentAccordion = $(accordion.next(0));
		if (this.options.direction == 'horizontal') {
			this.scaling = $H({
				scaleX: true,
				scaleY: false
			});
		} else {
			this.scaling = $H({
				scaleX: false,
				scaleY: true
			});			
		}
		if (this.currentAccordion.style.display == 'none') {
			this.activar();
		} else {
			this.desactivar();
		}
	},
	// 
	// Deactivate an active accordion
	//
	desactivar : function() {
		var options = $H({
			duration: this.duration,
			scaleContent: false,
			transition: Effect.Transitions.sinoidal,
			queue: {
				position: 'end', 
				scope: 'accordionAnimation'
			},
			scaleMode: { 
				originalHeight: this.options.defaultSize.height ? this.options.defaultSize.height : this.currentAccordion.scrollHeight,
				originalWidth: this.options.defaultSize.width ? this.options.defaultSize.width : this.currentAccordion.scrollWidth
			},
			afterFinish: function() {
				this.currentAccordion.setStyle({
					height: 'auto',
					display: 'none'
				});
				this.animating = false;
			}.bind(this)
		});    
		options.merge(this.scaling);
		this.currentAccordion.previous(0).removeClassName(this.options.classNames.toggleActive);
		new Effect.Scale(this.currentAccordion, 0, options);
	},
	
	activar : function() {
		var options = $H({
			duration: this.duration,
			scaleContent: false,
			transition: Effect.Transitions.sinoidal,
			queue: {
				position: 'end', 
				scope: 'accordionAnimation'
			},
			scaleMode: { 
				originalHeight: this.options.defaultSize.height ? this.options.defaultSize.height : this.currentAccordion.scrollHeight,
				originalWidth: this.options.defaultSize.width ? this.options.defaultSize.width : this.currentAccordion.scrollWidth
			},
			beforeStart: function() {		
			},
			afterFinish: function() {
				this.currentAccordion.setStyle({
					height: 'auto',
					display: 'block'
				});
				this.animating = false;
			}.bind(this)
		});    
		options.merge(this.scaling);
		this.currentAccordion.previous(0).addClassName(this.options.classNames.toggleActive);
		//new Effect.Scale(this.currentAccordion, 100, options);
		this.effects.push(
			new Effect.Scale(this.currentAccordion, 100, options)
		);
		
		new Effect.Parallel(this.effects, {
			duration: this.duration, 
			queue: {
				position: 'end', 
				scope: 'accordionAnimation'
			},
			beforeStart: function() {
				this.animating = true;
				this.showAccordion = this.currentAccordion;
				this.animating = false;				
			}			
		});
		
	},
	
	act : function(acor) {
		acor.addClassName(this.options.classNames.toggleActive);
		acor.next(0).setStyle({
			height: 'auto',
			display: 'block'
		});
	},
	
	desact : function(acor) {
		acor.removeClassName(this.options.classNames.toggleActive);
		acor.next(0).setStyle({
			height: 'auto',
			display: 'none'
		});
	}
}	

function activaTodos(container, class_name, class_name_active) {
	var acordiones = $$('#'+container+' .'+class_name);
	acordiones.each(function(acor) {
		acor.addClassName(class_name_active);
		acor.next(0).setStyle({
			height: 'auto',
			display: 'block'
		});
	});
}