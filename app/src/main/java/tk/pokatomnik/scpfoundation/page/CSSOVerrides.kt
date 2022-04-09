package tk.pokatomnik.scpfoundation.page

internal val css = """
    /* Hide those elements */
    #header { display: none !important; }
    #container-wrap { background: transparent !important; }
    .page-tags { display: none !important; }
    #page-info-break { display: none !important; }
    #page-options-container { display: none !important; }
    #action-area { display: none !important; }
    #footer { display: none !important; }
    #license-area { display: none !important; }
    #side-bar { display: none !important; }
    .page-rate-widget-box { display: none !important; }
    
    /* Style overrides */
    #page-content { min-height: 0 !important; }
    #main-content { margin: 0 20px !important; }
""".trimIndent()