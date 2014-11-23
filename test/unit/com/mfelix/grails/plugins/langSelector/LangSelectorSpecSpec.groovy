package com.mfelix.grails.plugins.langSelector

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(LangSelectorTagLib)
class LangSelectorSpecSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    @Unroll
    void "selector(): #langs #defaultLang #url"() {
        expect:
        tagLib.selector(langs: langs, default: defaultLang, url: url) == resultHtml
        where:
        langs                             | defaultLang | url          | resultHtml
        'es, en, en_US, pt_BR, pt, pt_pt' | null        | null         | ''
        'es, en, en_US, pt_BR, pt, pt_pt' | null        | 'libro/list' | ''
        'es, en, en_US, pt_BR, pt, pt_pt' | 'es'        | 'libro/list' | ''
    }

    @Unroll
    void "parseLocale(): #localeCode #locale"() {
        expect:
        tagLib.parseLocale(localeCode) == locale
        where:
        localeCode | locale
        'es'       | new Locale('es')
        'en_US'    | new Locale('en', 'US')
        'en-US'    | new Locale('en', 'US')
        'en-us'    | new Locale('en', 'US')
        'invalid!' | null
        null      | null
    }
}
