package br.com.astrosoft.engContratos.model.beans

class ProdutosNotaSaida(
  val loja: Int,
  val codigo: String,
  val refFor: String,
  val descricao: String,
  val grade: String,
  val qtde: Int,
  val valorUnitario: Double,
  val valorTotal: Double,
  val ipi: Double,
  val vst: Double,
  val valorTotalIpi: Double,
  val un: String,
  val st: String,
  val invno: Int,
  val vendno: Int,
                       ) {
  var item: Int = 0
}