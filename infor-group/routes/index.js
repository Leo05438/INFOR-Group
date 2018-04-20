require('../models/user')
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose')
var Users = mongoose.model('User')

/* GET home page. */
router.get('/', function(req, res, next) {

    Users.find({}).sort({score:-1}).exec(function(e,docs) {
      console.log(docs);
      res.locals.arr = docs
      res.locals.length = docs.length
      res.locals.i = 0
      res.locals.username = req.session.name ;
      res.locals.authenticated = req.session.logined;
      res.render( 'index', {title : '宇宙飛車'});
    })

});

module.exports = router;
