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

//用戶資訊(query.name,body.user,body.passwd)-->(html)
router.post('/userinfo',function(req, res, next){
  if ((!req.body.user) || (!req.body.passwd)) {
    console.log('資料不完整')
    res.json({
      error : true
    });
    return;
  }
  if (!req.query.name) {
    res.locals.username = req.body.user;
    res.locals.sessUsername = req.body.user;
    res.locals.passwd = req.body.passwd;
    Questions.find({name:req.body.user}).lean().exec(function(e,docs){
      res.locals.arr = docs;
      if (docs.length) {
        res.locals.length = docs.length;
      }
      else {
        res.locals.length = null;
      }
    });
    Users.findOne({username:req.body.user},function(e,doc){
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
          res.json({
            erro: true
          });
          return;
        }
      }
      res.locals.username = req.query.name;
      res.locals.sessUsername = req.body.user;
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
      Users.findOne({username:req.query.name},function(e,doc){
        res.locals.icon = doc.icon;
        res.locals.brief = doc.brief;
        res.render('users/userinfo');
      });
    });
  }
});

//編輯簡介(預設)(user)
router.post('/editBriefO',function(req, res, next){
  if (!req.body.user) {
    console.log('資料不完整')
    res.json({
      error : true
    });
    return;
  }
  Users.findOne({username:req.body.user},function(e,doc){
    if (doc) {
      res.json({
        brief : doc.brief
      });
      return;
    }
    else {
      res.json({
        error : "無使用者"
      });
      return;
    }
  })
});

//編輯簡介(user,passwd,brief)
router.post('/editBrief',function(req, res, next){
  if ((!req.body.user) || (!req.body.passwd) || (!req.body.brief)) {
    console.log('資料不完整')
    res.json({
      error : true
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
        if ( req.body.passwd != user.passwd ) {
          console.log('密碼錯誤');
          res.json({
            error: true
          });
          return;
        }
        console.log('登入成功');
        break
      }
      if (index == total-1) {
        console.log('名稱不存在');
        res.json({
          error: true
        });
        return;
      }
    }
    Users.update({username:req.body.user},{brief:req.body.brief},function(){
      res.json({
        error: false
      });
      return
    })
  });
});

//變更頭貼(user,passwd,imgfile)
// router.post('/editIcon', upload.single('upload'), function(req, res, next) {
//   console.log(req.body.link);
//   console.log(url.resolve('/upload/', req.body.link));
//   Users.find().lean().exec(function(e,docs){
//     var users = docs;
//     var index = 0;
//     var total = users.length;
//
//     for ( index ; index < total ; index++ ) {
//       var user = users[index];
//       if ( req.body.user == user.username ) {
//         if ( req.body.passwd != user.passwd ) {
//           console.log('密碼錯誤');
//           res.json({
//             error: true
//           });
//           return;
//         }
//         console.log('登入成功');
//         Users.update({username:req.body.user},{icon:url.resolve('/upload/', req.body.link)},function(){
//           res.json({
//             error: false
//           });
//           return
//         })
//       }
//       if (index == total-1) {
//         console.log('名稱不存在');
//         res.json({
//           error: true
//         });
//         return;
//       }
//     }
//   });
// });

//提問(query.category,user,passwd,title,content,)
router.post('/addQuestion',function(req, res, next){
  if ((!req.body.user) || (!req.body.passwd)|| (!req.body.title) || (!req.body.content)) {
    console.log('資料不完整')
    res.json({
      error : true
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
        if ( req.body.passwd != user.passwd ) {
          console.log('密碼錯誤');
          res.json({
            error: true
          });
          return;
        }
        console.log('登入成功');
        break;
      }
      if (index == total-1) {
        console.log('名稱不存在');
        res.json({
          error: true
        });
        return;
      }
    }
    if (!req.query.category || (req.query.category == "all")) {
      QuestionN.find().lean().exec(function(e,doc){
        var times = doc[0].times;
        var ntimes = times+1;
        QuestionN.update({times:times},{times:ntimes},function(){
          new Questions({
              name: req.body.user,
              title: req.body.title,
              content:req.body.content,
              id: ntimes,
              category: "all"
          }).save( function( err ){
              if (err) {
                  console.log('Fail to save to DB.');
                  return;
              }
              console.log('Save to DB.');
          });
          res.json({
            error: false
          });
          return;
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
            res.json({
              error: true
            });
            return;
          }
        }
        QuestionN.find().lean().exec(function(e,doc){
          var times = doc[0].times;
          var ntimes = times+1;
          QuestionN.update({times:times},{times:ntimes},function(){
            new Questions({
                name: req.body.user,
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
            res.json({
              error: false
            });
            return;
          });
        });
      })
    }
  });
});

//上傳圖片(imgfile)
// router.post('/upload', upload.single('upload'), function(req, res, next) {
//     console.log(url.resolve('/upload/', req.body.link));
//     res.json({
//         "url": url.resolve('/upload/', req.body.link)
//     });
// });

//編輯提問(預設)(query.id)
router.get('/editQuestionO',function(req, res, next){
  if (!req.query.id) {
    console.log('資料不完整')
    res.json({
      error : true
    });
    return;
  }
  Questions.findOne({id:req.query.id},function(e,doc){
    res.json({
      title : doc.title,
      content : doc.content,
      id : doc.id
    });
    return;
  })
});

//編輯提問(query.id,user,passwd,title,content)
router.post('/editQuestion',function(req,res,next){
  if ((!req.body.user) || (!req.body.passwd) || (!req.query.id) || (!req.body.title) || (!req.body.content)) {
    console.log('資料不完整')
    res.json({
      error : true
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
        if ( req.body.passwd != user.passwd ) {
          console.log('密碼錯誤');
          res.json({
            error: true
          });
          return;
        }
        console.log('登入成功');
        break;
      }
      if (index == total-1) {
        console.log('名稱不存在');
        res.json({
          error: true
        });
        return;
      }
    }
    QuestionN.find().lean().exec(function(e,times){
      Questions.find().lean().exec(function(e,docs){
        if (req.query.id > times[0].times) {
          res.json({
            error: true,
            erroType:1
          });
          return;
        }
        else {
          Questions.findOne({id:req.query.id},function(e,doc){
            if (req.body.user != doc.name) {
              res.json({
                error: true,
                erroType:2
              });
              return;
            }
            else {
              Questions.update({id:req.query.id},{title:req.body.title,content:req.body.content},function(){
                res.json({
                  error: false
                });
                return;
              });
            }
          });
        }
      });
    });
  });
});

//建立分類(預設)
router.get('/createCategoryO',function(req, res, next){
  Categories.find().lean().exec(function(e,docs){
    console.log(docs);
    res.json({
      category : docs
    });
    return;
  });
});

//建立分類(index)
router.post('/createCategory', function(req, res, next) {
    if (!req.body.index) {
      console.log('資料不完整')
      res.json({
        erro:1,
        erroType:1
      });
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
          res.json({
            erro:1,
            erroType:2
          });
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
          res.json({
            erro:0,
            erroType:0
          });
          return;
        }
      }
    });
});

//分類
router.get('/category',function(req, res, next){
  if ((req.query.index == "all") || (!req.query.index)) {
    Questions.find().lean().exec(function(e,docs){
      res.locals.arr = docs;
      res.locals.category = req.query.index;
      res.json({
        docs : docs,
        category : req.query.index
      });
    });
  }
  else {
    Questions.find({category:req.query.index}).lean().exec(function(e,docs){
      console.log(docs);
      res.locals.arr = docs;
      res.locals.category = req.query.index;
      res.json({
        docs : docs,
        category : req.query.index
      });
    });
  }
});

// 文章內容
// router.get('/detail',function(req, res, next){
//   if ((!req.body.user) || (!req.body.passwd)) {
//     console.log('資料不完整')
//     res.json({
//       error : true
//     });
//     return;
//   }
//   Questions.findOne({id:req.query.id},function(e,doc){
//     res.locals.doc = doc;
//     res.locals.name = req.session.name;
//     res.render('users/detail');
//   });
// });

//首頁
router.get('/index', function(req, res, next) {
  Categories.find().lean().exec(function(e,docs){
    console.log(docs);
    res.locals.arr = docs;
    res.json({
      docs : docs,
    });
  });
});

module.exports = router;
