package com.example.notescompose.di

import com.example.notescompose.data.repository.NotesRepoImpl
import com.example.notescompose.domain.repository.NotesRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepoModule {
    @Binds
    fun bindNotesRepo(notesRepo: NotesRepoImpl): NotesRepo
}