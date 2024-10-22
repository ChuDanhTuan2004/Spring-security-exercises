package com.example.jwtspring3.request;

import com.example.jwtspring3.model.library.Book;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookRequestSpecification implements Specification<Book> {
    private BookRequest bookRequest;

    public BookRequestSpecification(BookRequest bookRequest) {
        this.bookRequest = bookRequest;
    }

    @Override
    public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (bookRequest.getTitle() != null && !bookRequest.getTitle().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("title"), "%" + bookRequest.getTitle() + "%"));
        }
        if (bookRequest.getAuthor() != null && !bookRequest.getAuthor().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("author"), "%" + bookRequest.getAuthor() + "%"));
        }
        if (bookRequest.getPublisher() != null && !bookRequest.getPublisher().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("publisher"), "%" + bookRequest.getPublisher() + "%"));
        }
        if (bookRequest.getPublishYear() != 0) {
            predicates.add(criteriaBuilder.equal(root.get("publishYear"), bookRequest.getPublishYear()));
        }
        if (bookRequest.getCategoryId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("category").get("id"), bookRequest.getCategoryId()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
