require('../models/user');
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var url = require('url');
var path = require('path');
var multer = require('multer');
var Users = mongoose.model('User');
var Questions = mongoose.model('Question');
var Categories = mongoose.model('Category');
var QuestionN = mongoose.model('QuestionN');


//註冊
router.post('/loginr', function(req, res, next) {
    if ((!req.body.user) || (!req.body.passwd)) {
      console.log('資料不完整')
      req.session.rlErro = 1;
      req.session.rlErroType = 1;
      res.redirect('/users/register');
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
          req.session.rlErro = 1;
          req.session.rlErroType = 2;
          res.redirect('/users/register');
          return;
        }
        if (index == total-1) {
          req.session.name = req.body.user;
          req.session.passwd = req.body.passwd;
          req.session.logined = true;
          new Users({
              username: req.session.name,
              passwd: req.session.passwd,
              id: total+1,
              brief: "我討厭LL",
              icon:"/upload/1527943384479.jpeg"
          }).save( function( err ){
              if (err) {
                  console.log('Fail to save to DB.');
                  return;
              }
              console.log('Save to DB.');
          });
          console.log('註冊成功');
          res.redirect('/');
          return;
        }
      }
    });
});


//登入
router.post('/login', function(req, res, next) {
    if ((!req.body.user) || (!req.body.passwd)) {
      console.log('資料不完整');
      req.session.rlErro = 1;
      req.session.rlErroType = 1;
      res.redirect('/users/signin');
      return;
    }
    Users.find().lean().exec(function(e,docs){
      var users = docs;
      var index = 0;
      var total = users.length;
      for ( index ; index < total ; index++ ) {
        var user = users[index];
        if ( req.body.user == user.username ) {
          if ( req.body.passwd != user.passwd ) {
            console.log('密碼錯誤');
            req.session.rlErro = 1;
            req.session.rlErroType = 3;
            res.redirect('/users/register');
            return;
          }
          req.session.logined = true
          req.session.name = req.body.user
          req.session.passwd = req.body.passwd
          console.log('登入成功')
          res.redirect('/')
          return
        }
        if (index == total-1) {
          console.log('名稱不存在');
          req.session.rlErro = 1;
          req.session.rlErroType = 4;
          res.redirect('/users/register');
          return;
        }
      }
    });
});

//提問
router.post('/addQuestion',function(req, res, next){
    if ((!req.session.logined)||(!req.body.title)||(!req.body.content)) {
      res.redirect('/');
      return;
    }
    if (!req.query.category || (req.query.category == "all")) {
      QuestionN.find().lean().exec(function(e,doc){
        var times = doc[0].times;
        var ntimes = times+1;
        QuestionN.update({times:times},{times:ntimes},function(){
         Users.findOne({username:req.session.name},function(err,doc){
          //socket.emit("addquestion",req.session.name,req.body.title,req.body.content,200,ntimes,doc.icon)
          new Questions({
              name: req.session.name,
              title: req.body.title,
              content:req.body.content,
              id: ntimes,
              icon: doc.icon,
              category: "all"
          }).save( function( err ){
              if (err) {
                  console.log('Fail to save to DB.');
                  return;
              }
              console.log('Save to DB.');
          });

          res.redirect('/');
          return;
        })
        });
      });
    }
    else {
      Categories.find().lean().exec(function(e,categories){
          var index = 0;
          var total = categories.length;
          for ( index ; index < total ; index++ ) {
            var category = categories[index];
            if ( req.query.category == category.index ) {
              break;
            }
            else if (index == total-1) {
              res.redirect('/');
              return;
            }
          }
          QuestionN.find().lean().exec(function(e,doc){
            var times = doc[0].times;
            var ntimes = times+1;
            QuestionN.update({times:times},{times:ntimes},function(){
              new Questions({
                  name: req.session.name,
                  title: req.body.title,
                  content: req.body.content,
                  id: ntimes,
                  category: req.query.category
              }).save( function( err ){
                  if (err) {
                      console.log('Fail to save to DB.');
                      return;
                  }
                  console.log('Save to DB.');
              });
              res.redirect('/');
              return;
            });
          });
      })
    }
});


//上傳圖片
var storage = multer.diskStorage({
    destination: function(req, file, callback) {
        callback(null, './uploads');
    },
    filename: function(req, file, callback) {
        var name = Date.now() + path.extname(file.originalname);
        req.body.link = name;
        req.body.attachment = file.originalname;
        callback(null, name);
    }
});
var upload = multer({ storage: storage });

router.post('/upload', upload.single('upload'), function(req, res, next) {
    res.json({
        "uploaded": 1,
        "fileName": req.body.attachment,
        "url": url.resolve('/upload/', req.body.link)
    });
});


//更改密碼
router.post('/changePasswd',function(req, res, next){
  if ((!req.session.logined)||(!req.body.passwd) ){
    res.redirect('/');
    return;
  }
  Users.update({username:req.session.name},{passwd:req.body.passwd},function(){
    req.session.passwd = req.body.passwd;
    res.redirect('/users/userinfo');
  })
});


//編輯提問
router.post('/editQuestion',function(req,res,next){
  if ((!req.session.logined) || (!req.query.id)) {
    res.redirect('/');
    return;
  }
  QuestionN.find().lean().exec(function(e,times){
    Questions.find().lean().exec(function(e,docs){
      if (req.query.id > times[0].times) {
        res.redirect('/');
        return;
      }
      else {
        Questions.findOne({id:req.query.id},function(e,doc){
          if (req.session.name != doc.name) {
            res.redirect('/');
            return;
          }
          else {
            Questions.update({id:req.query.id},{title:req.body.title,content:req.body.content},function(){
              res.redirect('/');
              return;
            });
          }
        });
      }
    });
  });
});


//編輯簡介
router.post('/editBrief',function(req, res, next){
  if ((!req.session.logined)||(!req.body.brief)) {
    res.redirect('/');
    return;
  }
  Users.update({username:req.session.name},{brief:req.body.brief},function(){
    res.redirect('/users/userinfo');
  })
});


//建立分類
router.post('/createCategory', function(req, res, next) {
    if (!req.body.index) {
      console.log('資料不完整')
      req.session.ccErro = 1;
      req.session.ccErroType = 1;
      res.redirect('/users/createCategory');
      return;
    }
    Categories.find().lean().exec(function(e,docs){
      var ctgs = docs;
      var index = 0;
      var total = ctgs.length;
      for ( index ; index < total ; index++ ) {
        var ctg = ctgs[index];
        if ( req.body.index == ctg.index ) {
          console.log('分類已被使用');
          req.session.ccErro = 1;
          req.session.ccErroType = 2;
          res.redirect('/users/createCategory');
          return;
        }
        if (index == total-1) {
          new Categories({
              index: req.body.index,
              id: total+1
          }).save( function( err ){
              if (err) {
                  console.log('Fail to save to DB.');
                  return;
              }
              console.log('Save to DB.');
          });
          console.log('建立成功');
          res.redirect('/');
          return;
        }
      }
    });
});

//變更頭貼
router.post('/editIcon', upload.single('upload'), function(req, res, next) {
  Users.update({username:req.session.name},{icon:url.resolve('/upload/', req.body.link)},function(){
    res.redirect('/users/userinfo');
  })
});

module.exports = router;
