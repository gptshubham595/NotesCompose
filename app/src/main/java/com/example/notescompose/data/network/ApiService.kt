package com.example.notescompose.data.network

import com.example.notescompose.data.model.Note
import com.example.notescompose.data.model.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("notes")
    suspend fun getNotes(
    ): Response<List<NoteResponse>>

    @POST("notes/")
    suspend fun addNotes(
        @Body note: Note
    ): Response<NoteResponse>

    @DELETE("notes/{id}/")
    suspend fun deleteNote(
        @Path("id") id: Int
    ): Response<NoteResponse>

    @PUT("notes/{id}/")
    suspend fun updateNote(
        @Path("id") id: Int,
        @Body note: Note
    ): Response<NoteResponse>
}