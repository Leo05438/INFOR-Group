require('../models/user')
var express = require('express')
var router = express.Router()
var mongoose = require('mongoose')
var Users = mongoose.model('User')

router.post('/loginr', function(req, res, next) {
    if ((!req.body.user) || (!req.body.passwd)) {
      console.log('資料不完整')
      req.session.rlErro = 1
      req.session.rlErroType = 1
      res.redirect('/users/register')
      return
    }

    Users.find().lean().exec(function(e,docs){
      var users = docs
      var index = 0
      var total = users.length

      for ( index ; index < total ; index++ ) {
        var user = users[index]
        if ( req.body.user == user.username ) {
          console.log('名稱已被使用')
          req.session.rlErro = 1
          req.session.rlErroType = 2
          res.redirect('/users/register')
          return
        }
        if (index == total-1) {
          req.session.name = req.body.user
          req.session.passwd = req.body.passwd
          req.session.logined = true
          new Users({
              username: req.session.name,
              passwd: req.session.passwd
          }).save( function( err ){
              if (err) {
                  console.log('Fail to save to DB.')
                  return
              }
              console.log('Save to DB.')
          })
          console.log('註冊成功')
          res.redirect('/')
          return
        }
      }
    })
})

router.post('/login', function(req, res, next) {

    if ((!req.body.user) || (!req.body.passwd)) {
      console.log('資料不完整')
      req.session.rlErro = 1
      req.session.rlErroType = 1
      res.redirect('/users/signin')
      return
    }

    req.session.name = req.body.user

    Users.find().lean().exec(function(e,docs){
      var users = docs
      var index = 0
      var total = users.length

      for ( index ; index < total ; index++ ) {
        var user = users[index]
        if ( req.body.user == user.username ) {
          if ( req.body.passwd != user.passwd ) {
            console.log('密碼錯誤')
            req.session.rlErro = 1
            req.session.rlErroType = 3
            res.redirect('/users/register')
            return
          }
          req.session.logined = true
          console.log('登入成功')
          res.redirect('/')
          return
        }
        if (index == total-1) {
          console.log('名稱不存在');
          req.session.rlErro = 1
          req.session.rlErroType = 4
          res.redirect('/users/register')
          return
        }
      }
    })
})

module.exports = router