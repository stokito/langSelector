package com.mfelix.grails.plugins.langSelector

class LangSelectorTagLib {
    static namespace = 'langs'
    static returnObjectForTags = ['selectLang', 'generateUrl']

    /**
     * Render language selector. Examples:<br/>
     * {@code &lt;langs:selector langs="es, en, en_US, pt_BR, pt, pt_pt"/&gt;}
     * <p/>
     * {@code &lt;langs:selector langs="es, en, en_US, pt_BR, pt, pt_pt" url="${createLink(action: 'list', controller: 'libro', params: [paramun: 123])}"/&gt;}
     * <p/>
     * {@code &lt;langs:selector langs="es, en, en_US, pt_BR, pt, pt_pt" default="es" /&gt;}
     * <p/>
     * The required attribute "langs" tells the plugin which flags to show, if pay attention the values are the ISO 3166-1 alpha-2 code for languages and a countries, also are the same of the suffixes of "message properties" files.
     * <p/>
     * Optionally if you want to redirect always to the same url when changing the language (this is helpful to avoid doing a GET with post data) use the url parameter, and this provided url will be used instead of the actual one.
     * <p/>
     * From version 0.3 you optionally can set the default flag to be highlighted when the user enters for first time in the app or has a new fresh session.
     * <p/>
     * @attr langs REQUIRED Comma separated list of locales that available for selection.
     * @attr default Code of default locale
     * @attr url The url will be used instead of the actual one.
     */
    def selector = { attrs ->
        List<String> localeCodesList = attrs.langs?.toString()?.split(',')?.toList()*.trim()
        if (!localeCodesList) {
            throw new Exception("Error getting value of required attribute 'langs'. Accepted value for example is: es,en_US,en")
        }
        String defaultLang = attrs.default?.trim()
        String url = attrs.url?.trim()
        Locale selected = selectLang(defaultLang)
        url = generateUrl(url, selected)
        Map flags = getFlags(localeCodesList)
        // distinction selected or default style opacity
        out << render(template: '/langSelector/selector', plugin: 'langSelector', model: [flags: flags, selected: selected, uri: url])
    }

    Locale selectLang(String defaultLang) {
        Locale selected = (Locale) session["org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE"]
        // if not set in session, get it from attrs
        selected = selected ?: defaultLang ? Locale.forLanguageTag(defaultLang) : null
        // if no default is set get default locale
        selected = selected ?: Locale.getDefault()
        return selected
    }

    String generateUrl(String url, Locale selected) {
        if (!url) {
            url = request.requestURI + '?'
            String query = request.queryString?.replace('lang=' + selected.toString(), '') ?: ''
            if (query && !query.endsWith('&')) {
                query += '&'
            }
            url += query + 'lang='
        } else {
            url += url.contains('?') ? '&lang=' : '?lang='
        }
        return url
    }

    Map getFlags(List<String> localeCodesList) {
        Map<String, String> supported = StaticConfig.config
        Map flags = [:]
        localeCodesList.each { String localeCode ->
            try {
                Locale locale = Locale.forLanguageTag(localeCode)
                String country = locale.country ?: supported[locale.language]
                if (country) {
                    flags[localeCode] = country.toLowerCase()
                } else {
                    log.error "No country flag found for: ${locale.language} please check configuration."
                }
            } catch (Exception ex) {
                log.error("Can't parse locale ${localeCode}", ex)
            }
        }
        return flags
    }

    /** This tag includes the css stylesheet that helps you identify which language is selected */
    def resources = {
        out << """<link rel='stylesheet' href="${resource(plugin: 'langSelector', dir: 'css', file: 'langSelector.css')}" />"""
    }
}
