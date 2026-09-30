package no.nav.familie.ba.infotrygd.replika.model.converters

import jakarta.persistence.Converter

@Converter
class NavLocalDateConverter : AbstractNavLocalDateConverter("yyyyMMdd")
