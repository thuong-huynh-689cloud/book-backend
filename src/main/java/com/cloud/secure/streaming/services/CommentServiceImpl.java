package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.entities.Comment;
import com.cloud.secure.streaming.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getAllByAnnouncementIdOrderByCreatedDateDesc(String id) {
        return commentRepository.findAllByAnnouncementIdOrderByCreatedDateDesc(id);
    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Comment getById(String id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comment> getAllByAnnouncementIdIn(List<String> announcementIds) {
        return commentRepository.findAllByAnnouncementIdIn(announcementIds);
    }

    @Override
    public void deleteAllByAnnouncementIdIn(List<String> ids) {
        commentRepository.deleteAllByAnnouncementIdIn(ids);
    }

    @Override
    public List<Comment> getAllByIdIn(List<String> ids) {
        return commentRepository.findAllByIdIn(ids);
    }

    @Override
    public void deleteAllByIdIn(List<String> ids) {
        commentRepository.deleteAllByIdIn(ids);
    }
}
