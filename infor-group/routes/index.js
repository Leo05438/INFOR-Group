require('../models/user')
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose')
var Users = mongoose.model('User')

/* GET home page. */
router.get('/', function(req, res, next) {

    res.locals.username = req.session.name ;
    res.locals.authenticated = req.session.logined;
    res.render( 'index', {title : '宇宙飛車'});

});

module.exports = router;
