const mongoose = require('mongoose');
const express = require('express');
const Joi = require('joi');
const router = express.Router();
const {Post, validatePost} = require('../Models/post.js');

router.use(express.json());

router.get('/found', async (req, res) => {
    const post = await Post.find({status : "found"});
    res.send(post);
});

router.get('/lost', async(req, res) => {
    const post = await Post.find({status : "lost"});
    res.send(post);
});

router.post('/', async(req, res) => {

    const { error } = validatePost(req.body);

    if (error) return res.status(400).send(error.message);

    const post = Post({
        creatorId : req.body.creatorId,
        status : req.body.status,
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
