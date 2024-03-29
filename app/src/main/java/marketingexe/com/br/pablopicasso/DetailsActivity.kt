package marketingexe.com.br.pablopicasso

import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_top_bar.*
import kotlinx.android.synthetic.main.content_details.*
import marketingexe.com.br.pablopicasso.domain.Painting
import marketingexe.com.br.pablopicasso.extension.setStar
import marketingexe.com.br.pablopicasso.domain.TargetImage
import marketingexe.com.br.pablopicasso.extension.setStar


class DetailsActivity : MainActivity() {

    lateinit var painting: Painting
    lateinit var target: TargetImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)
        /*
         * Para apresentar na barra de topo a seta de "voltar a
         * atividade anterior"
         * */
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Iniciando a barra de topo personalizada.
        setAppBarHeight()

        // Obtendo o objeto Painting enviado via Intent.
        painting = intent.getParcelableExtra(Painting.KEY)

        /*
         * Debug para certificar que não está ocorrendo uma
         * nova requisção aos servidores remotos que contém
         * as imagens.
         * */
        /*Picasso
            .get()
            .setIndicatorsEnabled(true)*/

        /*
		 * Target é uma referência fraca (WikiPreference), então é preciso
		 * colocar um objeto deste tipo sendo referenciado de maneira forte,
		 * via variável ou propriedade, por exemplo.
		 * */
        target = TargetImage(
            iv_painting,
            painting.imageUrl,
            false // Não estamos em contexto de lista.
        )

        // Colocando a pintura.
        Picasso
            .get()
            .load(painting.imageUrl)
            .into( target )

        // Colocando os dados em texto.
        iv_painting.contentDescription = painting.name
        tv_year.text = painting.year.toString()
        tv_price.text = painting.getPriceBRFormat(
            getString(R.string.label_money_sign),
            getString(R.string.label_millions)
        )
        tv_details.text = painting.details

        // Colocando as estrelas.
        iv_star_01.setStar(painting.rating, 1)
        iv_star_02.setStar(painting.rating, 2)
        iv_star_03.setStar(painting.rating, 3)
        iv_star_04.setStar(painting.rating, 4)
        iv_star_05.setStar(painting.rating, 5)
    }

    /*
     * Para que a atualização do título do Toolbar seja
     * efetivo.
     */
    override fun onResume() {
        super.onResume()
        toolbar.title = painting.name
    }

    /*
     * Seguindo as recomendações da documentação e garantindo
     * que nenhuma requisição permanecerá, retendo a atividade
     * em memória para não ser recolhida pelo Garbage Collector.
     * */
    override fun onPause() {
        super.onPause()
        if( isFinishing ){

            /*
             * Cancele tanto as requisições ocorrendo no ImageView
             * desta atividade quanto no objeto Target utilizado
             * aqui.
             * */
            Picasso
                .get()
                .cancelRequest(iv_painting)
            Picasso
                .get()
                .cancelRequest( target )
        }
    }
}