package br.com.astrosoft.engContratos.model.beans

import java.time.LocalDate

class NotaSaida(
  val loja: Int,
  val sigla: String,
  val ni: Int,
  val nota: String,
  val fatura: String,
  val dataNota: LocalDate,
  val vendno: Int,
  val valor: Double,
  val obsNota: String,
  val remarks: String,
               )