require('../models/user');
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var Users = mongoose.model('User');
var Questions = mongoose.model('Question');

/* GET home page. */
router.get('/', function(req, res, next) {

    res.locals.username = req.session.name ;
    res.locals.authenticated = req.session.logined;
    Questions.find().lean().exec(function(e,docs){
      console.log(docs);
      res.locals.arr = docs;
      res.locals.length = docs.length;
      res.locals.i = 0;
      res.render( 'index');
    });


});

module.exports = router;
