require('../models/user');
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var Users = mongoose.model('User');
var Questions = mongoose.model('Question');

/* 使用者註冊頁面. */
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

/* 使用者登入頁面. */
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

/* 使用者登出頁面. */
router.get('/signout', function(req, res, next) {
  req.session.logined = false;
  res.redirect('/');
  res.end();
});

router.get('/userinfo',function(req, res, next){
  if (!req.session.logined) {
    res.redirect('/');
    return;
  }
  res.locals.username = req.session.name ;
  res.locals.passwd = req.session.passwd;
  //Users.findOne({username:req.session.name},function(e,doc){
    res.render('users/userinfo');
  //})
});

router.get('/addQuestion',function(req, res, next){
  if (!req.session.logined) {
    res.redirect('/');
    return;
  }
  res.render('users/addQuestion');
});

router.get('/changePasswd',function(req, res, next){
  if (!req.session.logined) {
    res.redirect('/');
    return;
  }
  res.locals.passwd = req.session.passwd;
  res.render('users/changePasswd');
});

router.get('/editQuestion',function(req, res, next){
  if (!req.session.logined) {
    res.redirect('/');
    return;
  }
  Questions.findOne({id:req.query.id},function(e,doc){
    res.locals.question = doc.question;
    res.locals.article = doc.article;
    res.render('users/editQuestion');
  })
});

router.get('/androidQuestions',function(req, res, next){
  Questions.find().lean().exec(function(e,docs){
    res.locals.arr = docs;
    res.locals.length = docs.length;
    res.locals.i = 0;
    res.render('users/androidQuestions');
  });
})

module.exports = router;
