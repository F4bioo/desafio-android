# PicPay - Desafio Android

<img src="https://github.com/mobilepicpay/desafio-android/blob/master/desafio-picpay.gif" width="300"/>

Um dos desafios de qualquer time de desenvolvimento é lidar com código legado e no PicPay isso não é diferente. Um dos objetivos de trazer os melhores desenvolvedores do Brasil é atacar o problema. Para isso, essa etapa do processo consiste numa proposta de solução para o desafio abaixo e você pode escolher a melhor forma de resolvê-lo, de acordo com sua comodidade e disponibilidade de tempo:
- Resolver o desafio previamente, e explicar sua abordagem no momento da entrevista.
- Resolver o desafio durante a entrevista, fazendo um pair programming interativo com os nossos devs, guiando o desenvolvimento.

Com o passar do tempo identificamos alguns problemas que impedem esse aplicativo de escalar e acarretam problemas de experiência do usuário. A partir disso elaboramos a seguinte lista de requisitos que devem ser cumpridos ao melhorar nossa arquitetura:

- Em mudanças de configuração o aplicativo perde o estado da tela. Gostaríamos que o mesmo fosse mantido.
- Nossos relatórios de crash têm mostrado alguns crashes relacionados a campos que não deveriam ser nulos sendo nulos e gerenciamento de lifecycle. Gostaríamos que fossem corrigidos.
- Gostaríamos de cachear os dados retornados pelo servidor.
- Haverá mudanças na lógica de negócios e gostaríamos que a arquitetura reaja bem a isso.
- Haverá mudanças na lógica de apresentação. Gostaríamos que a arquitetura reaja bem a isso.
- Com um grande número de desenvolvedores e uma quantidade grande de mudanças ocorrendo testes automatizados são essenciais.
  - Gostaríamos de ter testes unitários testando nossa lógica de apresentação, negócios e dados independentemente, visto que tanto a escrita quanto execução dos mesmos são rápidas.
  - Por outro lado, testes unitários rodam em um ambiente de execução diferenciado e são menos fiéis ao dia-a-dia de nossos usuários, então testes instrumentados também são importantes.

Boa sorte! =)


# Challenge result
## This is an example project using
- Jetpack components
- Retrofit
- Room Database
- DataStore
- Hilt Dependency Injection
- MVVM Architecture
- Paging 3, LoadStateAdapter
- ListAdapter
- Navigation, single Activity architecture
- SafeArgs
- Api rest

Response list
```sh
[
    {
        "id": "1",
        "name": "Sandrine Spinka",
        "img": "https://randomuser.me/api/portraits/men/1.jpg",
        "username": "Tod86"
    },
    ...
]
```

## Acknowledgements
* [Hilt dependency injection](https://developer.android.com/training/dependency-injection/hilt-android)

* [Navigation](https://developer.android.com/guide/navigation)

* [Safeargs](https://developer.android.com/guide/navigation/navigation-pass-data)

* [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines)

* [MVVM Architecture](https://developer.android.com/jetpack/guide)

* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)

* [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)

* [ListAdapter](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)

* [DiffUtil](https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil)

* [ViewBinding](https://developer.android.com/topic/libraries/view-binding)

* [Lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle)

* [Room](https://developer.android.com/training/data-storage/room)

* <del>[SharedPreferences](https://developer.android.com/training/data-storage/shared-preferences)</del>
  
* [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

* [Retrofit](https://square.github.io/retrofit/)

* [Espresso](https://developer.android.com/training/testing/espresso)
  
* [FragmentFactory](https://proandroiddev.com/android-fragments-fragmentfactory-ceec3cf7c959)

## Screens
<kbd>
<img src="images/main_screen.gif" width="30%"></img>
<img src="images/screen_capture0.png" width="30%"></img>
<img src="images/screen_capture1.png" width="30%"></img>
<img src="images/screen_capture2.png" width="30%"></img>
<img src="images/screen_capture3.png" width="30%"></img>
<img src="images/screen_capture4.png" width="30%"></img>
<img src="images/screen_capture5.png" width="30%"></img>
</kbd>
