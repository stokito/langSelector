package com.mfelix.grails.plugins.langSelector

import grails.test.mixin.TestFor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(LangSelectorTagLib)
class LangSelectorSpecSpec extends Specification {
    static final LOCALE_FROM_RESOLVER = new Locale('xx')
    static final ANY_LOCALE_CODE = 'yy'
    static final LOCALE_FROM_ATTR = new Locale('zz')

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

    @Unroll
    void "selectLang(): #defaultLocale #locale"() {
        given:
        session[SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME] = sessionLocale
        expect:
        tagLib.selectLang(defaultLocale) == locale
        where:
        sessionLocale        | defaultLocale             | locale
        LOCALE_FROM_RESOLVER | ANY_LOCALE_CODE           | LOCALE_FROM_RESOLVER
        null                 | LOCALE_FROM_ATTR.language | LOCALE_FROM_ATTR
        null                 | null                      | Locale.default
    }
}
