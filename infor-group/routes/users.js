require('../models/user');
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var Users = mongoose.model('User');
var Questions = mongoose.model('Question');
var Categories = mongoose.model('Category');


//註冊
router.get('/register', function(req, res, next) {
  if (req.session.logined) {
    res.redirect('/');
    return;
  }
  res.locals.rlErro = 0;
  res.locals.rlErroType = 0;
  if (req.session.rlErro == 1) {
    res.locals.rlErro = req.session.rlErro;
    res.locals.rlErroType = req.session.rlErroType;
  }
  req.session.rlErro = 0;
  res.render('users/register');
});


//登入
router.get('/signin', function(req, res, next) {
  if (req.session.logined) {
    res.redirect('/');
    return;
  }
  res.locals.rlErro = 0;
  res.locals.rlErroType = 0;
  if (req.session.rlErro == 1) {
    res.locals.rlErro = req.session.rlErro;
    res.locals.rlErroType = req.session.rlErroType;
  }
  req.session.rlErro = 0;
  res.render('users/signin');
});


//登出
router.get('/signout', function(req, res, next) {
  req.session.logined = false;
  res.redirect('/');
  res.end();
});


//用戶資訊
router.get('/userinfo',function(req, res, next){
  if (!req.session.logined) {
    res.redirect('/');
    return;
  }
  if (!req.query.name) {
    res.locals.username = req.session.name;
    res.locals.sessUsername = req.session.name;
    res.locals.passwd = req.session.passwd;
    Questions.find({name:req.session.name}).lean().exec(function(e,docs){
      res.locals.arr = docs;
      if (docs.length) {
        res.locals.length = docs.length;
      }
      else {
        res.locals.length = null;
      }
    });
    Users.findOne({username:req.session.name},function(e,doc){
      res.locals.icon = doc.icon;
      res.locals.brief = doc.brief;
      res.render('users/userinfo');
    });
  }
  else {
    Users.find().lean().exec(function(e,docs){
      var users = docs;
      var index = 0;
      var total = users.length;
      for ( index ; index < total ; index++ ) {
        var user = users[index];
        if ( req.query.name == user.username ) {
          break;
        }
        else if (index == total-1) {
          console.log("jizz");
          res.redirect('/');
          return;
        }
      }
      res.locals.username = req.query.name;
      res.locals.sessUsername = req.session.name;
      res.locals.passwd = null;
      Questions.find({name:req.query.name}).lean().exec(function(e,docs){
        res.locals.arr = docs;
        if (docs.length) {
          res.locals.length = docs.length;
        }
        else {
          res.locals.length = null;
        }
      });
      Users.find({username:req.query.name}).lean().exec(function(e,docs){
          res.locals.icon = docs[0].icon;
          res.locals.brief = docs[0].brief;
        res.render('users/userinfo');
      });
    });

  }
});


//提問
router.get('/addQuestion',function(req, res, next){
  if (!req.session.logined) {
    res.redirect('/');
    return;
  }
  res.locals.category = req.query.category;
  res.render('users/addQuestion');
});


//更改密碼
router.get('/changePasswd',function(req, res, next){
  if (!req.session.logined) {
    res.redirect('/');
    return;
  }
  res.locals.passwd = req.session.passwd;
  res.render('users/changePasswd');
});


//編輯提問
router.get('/editQuestion',function(req, res, next){
  if ((!req.session.logined) || (!req.query.id)) {
    res.redirect('/');
    return;
  }
  Questions.findOne({id:req.query.id},function(e,doc){
    res.locals.title = doc.title;
    res.locals.content = doc.content;
    res.locals.id = doc.id;
    res.render('users/editQuestion');
  })
});


//編輯簡介
router.get('/editBrief',function(req, res, next){
  if (!req.session.logined) {
    res.redirect('/');
    return;
  }
  Users.findOne({username:req.session.name},function(e,doc){
    res.locals.brief = doc.brief;
    res.render('users/editBrief');
  })
});


//建立分類
router.get('/createCategory',function(req, res, next){
  if (!req.session.logined) {
    res.redirect('/');
    return;
  }
  Categories.find().lean().exec(function(e,docs){
    res.locals.arr = docs;
    res.locals.length = docs.length;
    res.locals.i = 0;
    res.locals.ccErro = 0;
    res.locals.ccErroType = 0;
    if (req.session.ccErro == 1) {
      res.locals.ccErro = req.session.ccErro;
      res.locals.ccErroType = req.session.ccErroType;
    }
    req.session.ccErro = 0;
    res.render( 'users/createCategory');
  });
});


//分類
router.get('/category',function(req, res, next){
  if (!req.session.logined) {
    res.redirect('/');
    return;
  }
  if (req.query.index == "all") {
    Questions.find().lean().exec(function(e,docs){
      res.locals.arr = docs;
      res.locals.length = docs.length;
      res.locals.i = 0;
      res.locals.category = req.query.index;
      res.locals.name = req.session.name;
      res.render( 'users/category');
    });
  }
  else {
    Questions.find({category:req.query.index}).lean().exec(function(e,docs){
      res.locals.arr = docs;
      res.locals.length = docs.length;
      res.locals.i = 0;
      res.locals.category = req.query.index;
      res.locals.name = req.session.name;
      res.render( 'users/category');
    });
  }
});

//文章內容
router.get('/detail',function(req, res, next){
  if (!req.session.logined) {
    res.redirect('/');
    return;
  }
  Questions.findOne({id:req.query.id},function(e,doc){
    res.locals.doc = doc;
    res.locals.name = req.session.name;
    res.render('users/detail');
  });
});

module.exports = router;
