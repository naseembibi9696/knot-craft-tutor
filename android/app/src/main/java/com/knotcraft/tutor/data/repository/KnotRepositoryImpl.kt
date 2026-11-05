package com.knotcraft.tutor.data.repository

import com.knotcraft.tutor.data.sample.SampleData
import com.knotcraft.tutor.domain.model.KnotFamily
import com.knotcraft.tutor.domain.model.KnotSpec
import com.knotcraft.tutor.domain.repository.KnotRepository

class KnotRepositoryImpl : KnotRepository {

    override fun all(): List<KnotSpec> = SampleData.knots

    override fun byId(knotId: String): KnotSpec? = SampleData.knots.firstOrNull { it.id == knotId }

    override fun byFamily(family: KnotFamily): List<KnotSpec> =
        SampleData.knots.filter { it.family == family }
}
