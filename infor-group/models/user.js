var mongoose = require ( 'mongoose' );
var Schema = mongoose.Schema;

var userSchema = new Schema({
  username: String,
  passwd: String,
  id: Number,
  icon: String,
  brief: String,
  friend: Array
});

var questionSchema = new Schema({
  name: String,
  title: String,
  content:String,
  id: Number,
  created: {type: Date,default: Date.now },
  message: Array,
  messageN: Number,
  reply: Array,
  replyN: Number,
  reply_count: Number,
  category: String
});

var categorySchema = new Schema({
    index: String,
    id: Number
});

var questionNSchema = new Schema({
    times: Number
},{collection: 'questionN'});

mongoose.model( 'User' , userSchema);
mongoose.model( 'Question' , questionSchema);
mongoose.model( 'Category' , categorySchema);
mongoose.model( 'QuestionN' , questionNSchema);
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost/infor-group');
