var mongoose = require ( 'mongoose' );
var Schema = mongoose.Schema;

var userSchema = new Schema({
  username: String,
  passwd: String,
	score: Number
  },
  {
    versionKey: false // You should be aware of the outcome after set to false
  });

mongoose.model( 'User' , userSchema);
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost/work');
