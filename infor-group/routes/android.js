require('../models/user');
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var Users = mongoose.model('User');
var Questions = mongoose.model('Question');

//註冊
router.post('/loginr', function(req, res, next) {
    if ((!req.body.user) || (!req.body.passwd)) {
      console.log('資料不完整')
      res.json({
        registered : false,
        rErroType : 0
      });
      return;
    }
    Users.find().lean().exec(function(e,docs){
      var users = docs;
      var index = 0;
      var total = users.length;
      for ( index ; index < total ; index++ ) {
        var user = users[index];
        if ( req.body.user == user.username ) {
          console.log('名稱已被使用');
          res.json({
            registered : false,
            rErroType : 1
          });
          return;
        }
        if (index == total-1) {
          new Users({
              username: req.body.user,
              passwd: req.body.passwd
          }).save( function( err ){
              if (err) {
                  console.log('Fail to save to DB.');
                  return;
              }
              console.log('Save to DB.');
          });
          console.log('註冊成功');
          res.json({
            registered : true,
            rErroType : false
          });
          return;
        }
      }
    });
});

//登入
router.post('/login',function(req,res,next){

  Users.find().lean().exec(function(e,docs){
    var users = docs;
    var index = 0;
    var total = users.length;

    for ( index ; index < total ; index++ ) {
      var user = users[index];
      if ( req.body.user == user.username ) {
        if ( req.body.passwd != user.passwd ) {
          console.log('密碼錯誤');
          res.json({
            logined: false,
            lErroType: 0
          });
          return;
        }
        console.log('登入成功');
        res.json({
          logined: true,
          lErroType: false
        });
        return
      }
      if (index == total-1) {
        console.log('名稱不存在');
        res.json({
          logined: false,
          lErroType: 1
        });
        return;
      }
    }
  });
});

//更改密碼
router.post('/changePasswd',function(req, res, next){
  if ((!req.body.user) || (!req.body.oPasswd) || (!req.body.nPasswd)) {
    console.log('資料不完整')
    res.json({
      changed : false,
      cErroType : 0
    });
    return;
  }
  Users.find().lean().exec(function(e,docs){
    var users = docs;
    var index = 0;
    var total = users.length;

    for ( index ; index < total ; index++ ) {
      var user = users[index];
      if ( req.body.user == user.username ) {
        if ( req.body.oPasswd != user.passwd ) {
          console.log('密碼錯誤');
          res.json({
            changed: false,
            cErroType: 0
          });
          return;
        }
        console.log('登入成功');
        Users.update({username:req.body.user},{passwd:req.body.nPasswd},function(){
          res.json({
            changed: true,
            cErroType: false
          });
        });
        return
      }
      if (index == total-1) {
        console.log('名稱不存在');
        res.json({
          changed: false,
          cErroType: 1
        });
        return;
      }
    }
  });
});

module.exports = router;
