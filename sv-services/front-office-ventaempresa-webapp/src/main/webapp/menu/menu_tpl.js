var MENU_POS = [
{
	// item sizes
	'height': 20,
	'width': 140,
	// menu block offset from the origin:
	//	for root level origin is upper left corner of the page
	//	for other levels origin is upper left corner of parent item
	'block_top': 74,
	'block_left': 1,
	// offsets between items of the same level
	'top': 0,
	'left': 140,
	// time in milliseconds before menu is hidden after cursor has gone out
	// of any items
	'hide_delay': 200,
	'expd_delay': 200,
	'css' : {
		'outer': ['m0l0oout', 'm0l0oover'],
		'inner': ['m0l0iout', 'm0l0iover']
	}
},
{
	'height': 20,
	'width': 140,
	'block_top': 22,
	'block_left': 0,
	'top': 22,
	'left': 0,
	'css': {
		'outer' : ['m0l1oout', 'm0l1oover'],
		'inner' : ['m0l1iout', 'm0l1iover']
	}
},
{
	'height': 20,
	'width': 110,
	'block_top': 0,
	'block_left': 111,
	'css': {
		'outer': ['m0l2oout', 'm0l2oover'],
		'inner': ['m0l1iout', 'm0l2iover']
	}
}
]
