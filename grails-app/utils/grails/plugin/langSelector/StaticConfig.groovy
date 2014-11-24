package grails.plugin.langSelector

import grails.util.Holders

class StaticConfig {
    /** this static property can be overridden by config */
    static final LANG_FLAGS = [
        'es': 'es',
        'en': 'gb',
        'fr': 'fr',
        'da': 'dk',
        'de': 'de',
        'it': 'it',
        'ja': 'jp',
        'nl': 'nl',
        'ru': 'ru',
        'th': 'th',
        'zh': 'cn',
        'pt': 'pt'
    ]

    static Map<String, String> getConfig() {
        if (Holders.config.grails.plugin.langSelector.langFlags) {
            return Holders.config.grails.plugin.langSelector.langFlags
        } else if (Holders.config.com.mfelix.grails.plugins.langSelector.lang.flags) {
            log.warning('The option `com.mfelix.grails.plugins.langSelector.lang.flags` is renamed to `grails.plugin.langSelector.langFlags` and will be removed in v1.0. Please, don\'t forget to rename it.')
            return Holders.config.com.mfelix.grails.plugins.langSelector.lang.flags
        } else {
            return LANG_FLAGS
        }
    }
}
