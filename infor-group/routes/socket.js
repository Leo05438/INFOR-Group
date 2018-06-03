module.exports=function(io){
	var app=require("express");
	var router=app.Router();
	var ObjectId = require('mongodb').ObjectID;
	var moment=require("moment")
	var mongoose=require('mongoose');
	var Questions = mongoose.model('Question');
	var QuestionN = mongoose.model('QuestionN')
	io.on('connection',function(socket){ 
		console.log("a user jizzing")
		var sessionstring= socket.request.headers.cookie
		socket.on("addcontent1",function(msg,name,fromid,icon){ //回覆
			/*console.log('big user name: ' + name);
			console.log('new big content is: ' + msg);*/
			var newObjectId = String(ObjectId())
			//console.log(newObjectId)
			var newreply={id:newObjectId,content:msg,name:name ,time:moment().valueOf(),icon:icon,message:[]}
			Questions.findOneAndUpdate({id:fromid},{$push:{reply:newreply}},function(error,success){
				if(error){
					console.log("nojizz")
				}else{
					console.log("successjizz")
				}
			})
			//broadcast.emit('chat message',content)
			console.log(newObjectId)
			io.emit('addcontent1',msg,newObjectId,name,icon);
			//socket.broadcast.emit('addcontent',content);
			
		})
		socket.on("addcontent2",function(msg,fromindex,name,eid,fromid,icon){//回覆中留言
			/*console.log('small user name: ' + name);
			console.log('small content from: ' + eid);
			console.log('new small content is: ' + msg);*/
			var newObjectId = String(ObjectId())
			var newmessage={id:newObjectId,content:msg,name:name,time:moment().valueOf(),icon:icon}
			Questions.findOneAndUpdate({id:fromid,"reply.id":eid},{$push:{"reply.$.message":newmessage}},function(error,doc){
				if(error){
					console.log("nojizz")
				}else{
					
					console.log("successjizz")
				}
			})
			io.emit('addcontent2',msg,fromindex,newObjectId,name,icon);
		})
		socket.on("addcontent3",function(msg,name,fromid,icon){//問題中留言
			console.log("fromid")
			var newObjectId=String(ObjectId())
			var newmsg={id:newObjectId,content:msg,name:name,time:moment().valueOf(),icon:icon}
			Questions.findOneAndUpdate({id:fromid},{$push:{message:newmsg}},function(err,success){
				if(err){
					console.log("nojizz")
				}else{
					console.log("successjizz")
				}
			})
			io.emit("addcontent3",msg,name,newObjectId,icon)
		})
		socket.on("removecontent1",function(index,eid,fromid){//刪除回覆
			console.log(eid)
			Questions.findOneAndUpdate({id:fromid},{$pull:{reply:{id:eid}}},function(error,success){
				if(error){
					console.log("nojizz")
				}else{
					console.log("successjizz")
				}
			})
			io.emit('removecontent1',index)
		})
		socket.on("removecontent2",function(fromindex,index,eid,fromid,fromfromid){//刪除回覆中留言
			Questions.findOneAndUpdate({id:fromfromid,"reply.id":fromid},{$pull:{ "reply.$.message" :{id:eid}}},function(error,success){
				if(error){
					console.log("nojizz")
				}else{
					console.log("successjizz")
				}
			})
			io.emit('removecontent2',fromindex,index)
		})	
		socket.on("removecontent3",function(index,eid,fromid){//刪除問題中留言
			Questions.findOneAndUpdate({id:fromid},{$pull:{message:{id:eid}}},function(err,success){
				if(err){
					console.log("nojizz")
				}else{
					console.log("successjizz")
				}
			})
			io.emit("removecontent3",index)
		})
		socket.on("removecontent4",function(eid){//移除頁面
			console.log(eid)
			Questions.deleteOne({ id : eid },function(err){
			if(err) return handleError(err);
			})
			/*Questions.findOneAndUpdate({id:eid},{$pull:{id:eid}},function(err,success){
				if(err){
					console.log(err)
				}else{
					console.log("successjizz")
				}
			})*/
			io.emit("removecontent4")
		})
		socket.on("addquestion",function(){
			setTimeout(function(){
				QuestionN.find().lean().exec(function(e,docs){
				Questions.findOne({id:docs[0].times+1},function(err,doc){
					if(err){
						console.log("nonojizz")
					}else{
						console.log(docs)
						console.log(doc)
						io.emit("addquestion",doc.name,doc.title,doc.content,doc.time,doc.id,doc.icon)
					}
				})
			})},5000)

			
			
		})
		socket.on("removequestion",function(index,eid){
			Questions.deleteOne({ id : eid },function(err){
			if(err) return handleError(err);
			})
			io.emit("removequestion",index)
		})
		
	})
	return router;
}
