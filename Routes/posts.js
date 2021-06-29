const mongoose = require('mongoose');
const express = require('express');
const Joi = require('joi');
const router = express.Router();
const {Post, validatePost} = require('../Models/post.js');

router.use(express.json());

router.post('/', async(req, res) => {

    const { error } = validatePost(req.body);

    if (error) return res.status(400).send(error.message);

    const post = Post({
        creatorId : req.body.creatorId,
        creatorName : req.body.creatorName,
        category : req.body.category,
        creationDate : req.body.creationDate,
        mediaUrl : req.body.mediaUrl,
        description : req.body.description
    });

    await post.save();
    return res.send("Post Uploaded");
});

module.exports = router;
