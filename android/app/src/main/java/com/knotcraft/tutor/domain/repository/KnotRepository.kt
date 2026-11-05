package com.knotcraft.tutor.domain.repository

import com.knotcraft.tutor.domain.model.KnotFamily
import com.knotcraft.tutor.domain.model.KnotSpec

interface KnotRepository {
    fun all(): List<KnotSpec>
    fun byId(knotId: String): KnotSpec?
    fun byFamily(family: KnotFamily): List<KnotSpec>
}
