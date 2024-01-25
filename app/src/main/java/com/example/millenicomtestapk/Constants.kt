package com.example.millenicomtestapk

object Constants {
    const val SHARED_PREFERENCES_NAME = "com.wireless.ambeent.ambeentmini.preferences"
    const val GROUP_CODE = "group_code"
    const val USERNAME = "customer"
    const val DEFAULT_USERNAME = "Ambeent"
    const val COMPANY_ID = "company"
    const val DEFAULT_COMPANY_ID = "05a99899-3f0e-40f0-9d11-167ef193e0a6"
    const val AMB_DEVICE_ID = "device_id"
    const val APP_STATUS_FOREGROUND = "app_status_foreground"
    const val IS_FIRST_INIT_KEY = "is_first_init"
    const val USER_AGREED_KEY = "user_agreed"
    const val TARGET_FIDELITY = "target_fidelity"
    const val TARGET_FIDELITY_TIME = "target_fidelity_time"
    const val LAST_DISCOVERY_TIME = "last_discovery_time"
    const val LOCATION_UPDATE_ACTION = "com.wireless.ambeent.micro.LOCATION_UPDATE"
    const val ALARM_ACTION = "net.ambeent.app.android.ALARM"
    const val UPNP_MSEARCH_QUERY = "M-SEARCH * HTTP/1.1\r\n" +
            "HOST: 239.255.255.250:1900\r\n" +
            "MAN: \"ssdp:discover\"\r\n" +
            "MX: 1\r\n" +  //"ST: urn:schemas-upnp-org:service:AVTransport:1\r\n" +  // Use for Sonos
            //"ST: ssdp:all\r\n" +  // Use this for all UPnP Devices
            "ST: urn:schemas-upnp-org:device:InternetGatewayDevice:1\r\n" +
            "\r\n"
    const val UPNP_AllSEARCH_QUERY = "M-SEARCH * HTTP/1.1\r\n" +
            "HOST: 239.255.255.250:1900\r\n" +
            "MAN: \"ssdp:discover\"\r\n" +
            "MX: 1\r\n" +  //"ST: urn:schemas-upnp-org:service:AVTransport:1\r\n" +  // Use for Sonos
            "ST: ssdp:all\r\n" +  // Use this for all UPnP Devices
            //"ST: urn:schemas-upnp-org:device:InternetGatewayDevice:1\r\n" +
            "\r\n"

    // Simple Service Discovery Protocol Address for UPnP
    const val UPNP_SSDP_ADDRESS = "239.255.255.250"
    const val APP_CALLBACK_PORT = 8081
    const val APP_CALLBACK_POR_2 = 8083
    const val DOWNSTREAM_CALLBACK_PORT = 8082

    // Supported brands
    const val CH7465LG = "CH7465LG"
    const val ACTIONTEC = "Actiontec"
    const val ARRIS = "Arris"
    const val AIRTIES = "AirTies"
    const val ASUS = "Asus"
    const val BILLION = "Billion"
    const val COMPAL = "Compal"
    const val EFM = "Efm"
    const val EZNET = "EZ-NET"
    const val HUAWEI = "Huawei"
    const val HUMAX = "Humax"
    const val MOTOROLA = "Motorola"
    const val REALTEK = "Realtek Semiconductor"
    const val ROUTERBOARD = "Routerboardcom"
    const val SAMSUNG = "Samsung"
    const val TPLINK = "TP-Link"
    const val TWOWIRE = "2Wire"
    const val VERIZON = "Verizon"
    const val ZYXEL = "ZyXEL"
    const val ZTE = "Zte"
    const val TENDA = "Tenda"
    const val Linux = "Linux"
    const val NETGEAR = "NETGEAR" + " "
    const val DLINK = "D-Link"
    const val LINKSYS = "Linksys"
    const val SERCOMM = "Sercomm"
    const val SAGEMCOM = "Sagemcom"
    const val SAGEMCOMUPPER = "SAGEM"
    const val CASTLENET = "Castlenet"
    const val NOKIA = "Nokia"
    const val DARK = "Dark Networking TECHNOLOGY CO., LTD."
    const val UBUNTU = "Ubuntu"
    const val CLARO = "Claro"
    const val EDGECORE = "Edgecore"


    //Zte brand
    //public static final String BROADCOM = "Broadcom";
    //Unknown brand
    const val UNKNOWN = "unknown"

    //Actiontec models
    const val GT784WNV = "GT784WNV"
    const val GT784WN = "GT784WN"
    const val MI424WR = "\"Wireless Broadband Router\""

    //Linux // Verizon
    const val FiOS_G1100 = "Linux Router"

    // Arris models
    const val NVG510 = "NVG510"
    const val NVG599 = "NVG599"
    const val NVG589 = "NVG589"
    const val SBG6700AC = "SBG6700-AC"
    const val SBRAC1750 = "ARRIS SBR-AC1750 Router"
    const val TG862G = "ARRIS TG862G Router"
    const val FXN5268AC = "5268AC FXN"
    const val TG862A = "ARRIS TG862A Router"

    // Airties models
    const val AIR5750 = "Air5750"
    const val AIR5750TR_VN = "Air5750TR-VN"
    const val DSL_N16 = "DSL-N16"
    const val AIR5444TT = "Air5444TT"
    const val RT_206v4TT = "RT-206v4TT"
    const val RT_204v2 = "RT-204v2"
    const val RT_205 = "RT-205"
    const val AIR5650v3TT = "Air5650v3TT"
    const val AIR5650v2TT = "Air5650v2TT"
    const val AIR5650 = "Air5650"
    const val AIR5650TT = "Air5650TT"
    const val AIR5341 = "Air5341"
    const val AIR5443 = "Air5443"
    const val AIR5343 = "Air5343v2"
    const val AIR5442 = "Air5442"
    const val AIR5442VN = "Air5442VN"
    const val AIR4450 = "Air4450"
    const val AIR6372 = "Air6372"
    const val AIR5342 = "Air5342"
    const val AIR5452 = "Air5452"

    // Edgecore models
    const val ECW5211_L = "ECW5211-L"

    // Asus models
    const val DSL_AC51 = "DSL-AC51"
    const val DSL_AC52U = "DSL-AC52U"
    const val DSL_N17U = "DSL-N17U"

    // Billion models
    const val NXL8400R2 = "Billion 8400NXL R2"

    // Castlenet models
    const val INFINITY401 = "Infinity401_V"


    //D-Link Models
    const val DSL_2750U = "DSL-2750U"
    const val DSL_2740U = "DSL-2740U"

    // Linksys models
    const val WAG310g = "WAG310g"

    // EFM model
    const val N704BCM = "ipTIME N704BCM"
    const val A604M = "ipTIME A604M"
    const val A604 = "ipTIME A604"
    const val A604G_MU = "ipTIME A604G-MU"

    // EZNET models
    const val NEXT504N = "NEXT-504N"

    // Huawei models
    const val HG253S = "HG253s"
    const val HG255S = "HG255s"
    const val HG552e = "HG552e"
    const val HG655d = "HG655d"
    const val HG658V2 = "HG658 V2"
    const val HG630a = "HG630a"
    const val HG532s = "HG532s"
    const val HG556a = "Huawei_HGW"
    const val HG531s = "HG531s V1"
    const val HG658c = "HG658c"
    const val HG658cV2 = "HG658c V2"
    const val EchoLife_EG8145V5 = "EchoLife EG8145V5"

    // Humax models
    const val CH8568 = "CH8568"
    const val HGA12R = "Ubuntu router"
    const val HGB10R = "BCM9338x"

    //Tenda models
    const val D301_v2 = "D301_v2"
    const val V300 = "V300"
    const val Tenda_V300 = "Tenda_V300"
    const val F3 = "Wireless-N Router"
    const val D305 = "D305"
    const val N301 = "Wireless-N Router"
    const val V1200 = "V1200"

    // Everest (Mac model Tenda)
    const val SG_1600 = "SG-1600"
    const val SG_V300 = "SG-V300"

    // Realtek Semiconductor models
    const val IGD = "IGD"
    const val WRT302 = "WRT302"

    // Routerboard.com models
    const val ROUTEROS = "Router OS"

    // Motorola models
    const val MG7315 = "MG7315"
    const val MG7550 = "MG7550"

    // NetGear models
    const val R6080 = "NETGEAR R6080 Router"

    // Nokia models
    const val G_240W_C = "IGD Version 2.00"

    // Samsung models
    const val SWW3100BG = "SWW-3100BG"

    //Sercomm models
    const val VodafoneH_300s = "Vodafone H 300s"

    //Sagemcom models

    const val LiveBoxAc22 = "Livebox Pro"
    const val Fatst3686V1B = "F@ST3686"

    // TPLink models
    const val ArcherC6v2 = "ArcherC6v2"
    const val VN020G2u = "VN020-G2u"
    const val VC220G3u = "VC220-G3u"
    const val VN020F3 = "VN020-F3"
    const val TDW8951ND = "TD-W8951ND"
    const val TDW8961N = "TD-W8961N IGD"
    const val TDW9970 = "TD-W9970"
    const val TDW9960 = "TD-W9960"
    const val TDW9970_TR = "TD-W9970_TR"
    const val TDW864 = "TD864W IGD"
    const val TDW854 = "TD854W IGD"
    const val ARCHERMR200 = "Archer_MR200"
    const val ARCHER_C9 = "Archer C9"
    const val ARCHER_C7 = "Archer C7"
    const val ARCHER_C7_AC1750 = "Archer C7 | AC1750"
    const val ARCHER_C1200_AC1200 = "Archer AC1200"
    const val ARCHER_C1200 = "Archer C1200 | AC1200"
    const val ARCHERVR300 = "ARCHER_VR300"
    const val TLWR840N = "TL-WR840N"
    const val TLWR841N = "TL-WR841N"
    const val TLWR842N = "TL-WR842N"
    const val TLWR740N = "TL-WR740N"
    const val EAP110 = "EAP110"
    const val EAP225 = "EAP225"
    const val ArcherC20 = "Archer_C20"
    const val AD7200 = "AD7200 (BETA)"
    const val Archer_A10 = "Archer A10 (BETA)"
    const val Archer_A20 = "Archer A20 (BETA)"
    const val Archer_A2300 = "Archer A2300 (BETA)"
    const val Archer_A9 = "Archer A9 (BETA)"
    const val Archer_AX6000 = "Archer AX6000 (BETA)"
    const val Archer_C1900 = "Archer C1900 (BETA)"
    const val Archer_C2300 = "Archer C2300 (BETA)"
    const val Archer_C3000 = "Archer C3000 (BETA)"
    const val Archer_C3150_V2 = "Archer C3150 V2 (BETA)"
    const val Archer_C3200 = "Archer C3200 (BETA)"
    const val Archer_C4000 = "Archer C4000 (BETA)"
    const val Archer_C50 = "Archer_C50"
    const val Archer_C5200 = "Archer C5200 (BETA)"
    const val Archer_C5400 = "Archer C5400 (BETA)"
    const val Archer_C5400X = "Archer C5400X (BETA)"
    const val Archer_C59 = "Archer C59 (BETA)"
    const val Archer_C900 = "Archer C900 (BETA)"
    const val TL_WR1043N = "TL-WR1043N (BETA)"
    const val Touch_P5 = "Touch P5 (BETA)"
    const val Archer_VR1200 = "Archer_VR1200"
    const val TP_Link_router = "TP-Link router"
    const val ArcherC5V = "Archer_C5v"

    // 2wire models
    const val HGVB3800 = "3800HGV-B"
    const val NV5031_030 = "5031NV-030"

    // Xiaomi models
    const val D38C = "D38C"

    //     Verizon models
    //    public static final String FiOS_G1100 = "FiOS-G1100";
    // Zte models
    const val ZXHNH267N = "ZXHN H267N"
    const val ZXHNH108NV2 = "ZTE HG"
    const val ZXV10_W300 = "ZXV10 W300 IGD"
    const val ZXHN_H168N = "ZXHN H168N"

    // Zyxel models
    const val VMG3312B10B = "VMG3312-B10B"
    const val VMG3312B10A = "VMG3312-B10A"
    const val P1302T10DV3 = "P-1302-T10D v3"
    const val VMG1312T20B = "VMG1312-T20B"
    const val VMG1312B10D = "VMG1312-B10D"
    const val VMG3313B10A = "VMG3313-B10A"
    const val P660NT1A = "P-660N-T1A"
    const val P660WT1 = "ZyXEL Internet Sharing Gateway"
    const val WAP3205V3 = "WAP3205 v3 IGD Version 1.00"
    const val NBG6604 = "Zyxel NBG6604 Router"
    const val VMG8623T50B = "VMG8623-T50B"

    // Fidelity diff for measurement upload
    const val FIDELITY_DIFF = 5

    // Device brand names
    const val XIAOMI = "Xiaomi"
    const val XIAOMI_PERMISSION_PACKAGE = "com.miui.securitycenter"
    const val XIAOMI_PERMISSION_CLASS =
        "com.miui.permcenter.autostart.AutoStartManagementActivity"

    // Analytics events
    const val USER_AGREED_EVENT = "user_agreed"
    const val USER_NOT_AGREED_EVENT = "user_not_agreed"
    const val LOCATION_PERMISSION_GRANTED_EVENT = "location_permission_granted"
    const val LOCATION_PERMISSION_DENIED_EVENT = "location_permission_denied"
    const val HELP_CLICK_EVENT = "help_clicked"
    const val OPTIMIZE_CLICK_EVENT = "optimize_clicked"
    const val DASHBOARD_CLICK_EVENT = "dashboard_clicked"
    const val MODEL_CLICK_EVENT = "model_clicked"
    const val MODEL_PICKER_SHOWN_EVENT = "model_picker_shown"
    const val MODEL_SAVE_EVENT = "user_saved_router_model"
    const val CREDENTIALS_DIALOG_SHOWN_EVENT = "credentials_dialog_shown"
    const val CREDENTIALS_SAVE_EVENT = "user_saved_credentials"
    const val REQUEST_DIALOG_SHOWN_EVENT = "request_dialog_shown"
    const val REQUEST_SAVE_EVENT = "user_saved_request"
    const val OPTIMIZATION_SUCCEEDED_EVENT = "optimization_succeeded"
    const val DOWNSTREAM_OPTIMIZATION_SUCCEEDED = "downstream_optimization_success"
    const val DOWNSTREAM_OPTIMIZATION_FAILED = "downstream_optimization_failed"
    const val UPNP_DISCOVERY_EVENT = "upnp_discovery"
    const val AUTO_CONNECT_TO_DIFFERENT_ROUTER = "auto_connect_to_different_router"
    const val MAXIMUM_RETRY_AFTER_CHANNEL_CHANGE = "max_retry_after_channel_change"
    const val POST_YOUTUBE_EVENT = "post_youtube_event"
    const val POST_SPEED_EVENT = "post_youtube_event"
    const val UPNP_ANALYTICS_PARAM = "post_youtube_event"
    const val POST_SCAN_RESULT_EVENT = "post_scan_result_param"
    const val REQUEST_EVENT = "post_request_email_model_param"

    // Analytics params
    const val BRAND_PARAM = "brand"
    const val MODEL_PARAM = "model"
    const val OLD_CHANNEL_PARAM = "old_channel"
    const val NEW_CHANNEL_PARAM = "new_channel"
    const val MANUFACTURER_PARAM = "manufacturer"
    const val MODEL_NAME_PARAM = "model_name"
    const val MODEL_NUMBER_PARAM = "model_number"
    const val FAILURE_DESCRIPTION_PARAM = "failure_description"
    const val CURRENT_CHANNEL_PARAM = "current_channel"
    const val ROUTER_MAC_ADDRESS_PARAM = "router_mac_address"
    const val SPEED_PARAM = "speed"
    const val YOUTUBE_PARAM = "youtube_param"
    const val SCAN_RESULT_PARAM = "youtube_param"
    const val ROUTER_MODEL_PARAM = "youtube_param"
    const val REQUEST_PARAM = "request_param"
    const val BRAND_MODEL_MAC_PARAM = "brand_model_mac_param"

    // Remote config params
    const val MIN_COEF = "min_coef"
    const val MIN_ADD = "min_add"
    const val MAX_COEF = "max_coef"
    const val MAX_ADD = "max_add"
    const val MEASUREMENT_HISTORY = "measurement_history"

    const val FIRST_LAUNCH = "first_launch"
    const val FIRST_LAUNCH_BACKGRROUND_LOCATION = "first_launch_background_location"
    const val BACKGROUND_PERMISSION = "background_permission"
    const val FIREBASE_TOKEN = "firebase_token"
    const val SAVED_NETWORKS = "saved_networks"
    const val MESH_NETWORKS = "mesh_networks"
    const val MESH_BLACK_LIST = "mesh_black_list"
}