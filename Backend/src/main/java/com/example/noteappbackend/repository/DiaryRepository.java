package com.example.noteappbackend.repository;
import com.example.noteappbackend.entity.Diary;
import com.example.noteappbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByUser(User user);
}
