package com.cms.bff.dibericky.models

open class CustomViews(
    val id: String,
    val name: String,
    val type: String
)

class GalleryCustomViews(
    id: String,
    name: String,
    val source: String,
    val collection: String,
    val projection: List<String>,
    val metadata: List<String>,
    val categorizedBy: String?
) : CustomViews(id=id, name=name, type="gallery")