package com.cms.bff.dibericky

import com.cms.bff.dibericky.models.CustomViews
import com.cms.bff.dibericky.services.CrudService
import com.cms.bff.dibericky.services.CustomViewsService
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.jackson.JacksonConverter
import kotlinx.coroutines.*

fun String.asResource() = this.javaClass::class.java.getResource(this).readText()

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val customViewCrudService = CrudService<CustomViews>(DbMock.customViews)
val customViewsService = CustomViewsService(customViewCrudService)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Locations) {
    }
    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter())
    }

    val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }
    runBlocking {
        // Sample for making a HTTP Client request
        /*
        val message = client.post<JsonSampleClass> {
            url("http://127.0.0.1:8080/path/to/endpoint")
            contentType(ContentType.Application.Json)
            body = JsonSampleClass(hello = "world")
        }
        */
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }

        get<LocationCollections>{
            val json = "/mocks_data/collections.json".asResource()
            call.respondText(json)
        }
        get<LocationConfigs>{
            val json = "/mocks_data/configs.json".asResource()
            call.respondText(json)
        }
        get<LocationCustomViews>{
           val list = customViewsService.getList()
            val map : MutableMap<String, CustomViews> = mutableMapOf()
            list.forEach { map.putIfAbsent(it.id, it) }
            call.respond(map)
        }

        get<MyLocation> {
            call.respondText("Location: name=${it.name}, arg1=${it.arg1}, arg2=${it.arg2}")
        }
        // Register nested routes
        get<Type.Edit> {
            call.respondText("Inside $it")
        }
        get<Type.List> {
            call.respondText("Inside $it")
        }
    }
}

@Location("/collections")
class LocationCollections()

@Location("/configs")
class LocationConfigs()

@Location("/custom-views")
class LocationCustomViews()


@Location("/location/{name}")
class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")

@Location("/type/{name}") data class Type(val name: String) {
    @Location("/edit")
    data class Edit(val type: Type)

    @Location("/list/{page}")
    data class List(val type: Type, val page: Int)
}

data class JsonSampleClass(val hello: String)

