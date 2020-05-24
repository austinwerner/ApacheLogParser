package android.werner.apachelogparser.util

object Constants {

    const val URL = "https://dev.inspiringapps.com/Files/IAChallenge/30E02AAA-B947-4D4B-8FB6-9C57C43872A9/Apache.log"

    const val APACHE_LOG_REGEX = "^(\\S+) (\\S+) (\\S+) " +
            "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)" +
            " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";

    const val HTTP_OK = 200
}