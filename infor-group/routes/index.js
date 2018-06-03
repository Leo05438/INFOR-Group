require('../models/user');
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var Users = mongoose.model('User');
var Questions = mongoose.model('Question');
var Categories = mongoose.model('Category');

//home page
router.get('/', function(req, res, next) {

    res.locals.username = req.session.name;
    res.locals.authenticated = req.session.logined;
     Categories.find().lean().exec(function(e,docs){
      res.locals.arr = docs;
      res.locals.length = docs.length;
      res.locals.i = 0;
      if(req.session.logined){
      	Users.findOne({username:req.session.name},function(err,doc){
      		if(err){
      			console.log("nononojizz")
      		}else{
      			res.locals.usericon=doc.icon
      			res.render('index');
      		}
      		
      	})
    	
    }else{
    	res.render( 'index');
    }

      
    });


});

module.exports = router;
