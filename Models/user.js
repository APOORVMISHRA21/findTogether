const mongoose = require('mongoose');
const Joi = require('joi');

const schema = Joi.object({
    email : Joi.string().required(),
    password : Joi.string().required()
});

function validateUser(user) {
    return schema.validate(user);
}

const userSchema = new mongoose.Schema({

    userId : {
        type : Number,
        required : true
    },

    email : {
        type : String,
        required : true,
        unique : true
    },

    password : {
        type : String,
        required : true,
        minlength : 5
    }
});


const User = mongoose.model('User', userSchema);

module.exports.User = User;
module.exports.validateUser = validateUser;