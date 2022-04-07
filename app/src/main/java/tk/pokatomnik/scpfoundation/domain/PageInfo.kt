package tk.pokatomnik.scpfoundation.domain

interface PageInfo {
    val name: String;
    val url: String;
    val rating: Int?;
    val author: String?;
    val date: String?;
}
