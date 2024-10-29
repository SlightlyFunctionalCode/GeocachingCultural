# Âmbito:
O objetivo do Geocaching Cultural é promover a cultura através de um desafio de geocaching colaborativo. Os utilizadores devem explorar locais culturais em cidades ou áreas específicas, seguindo pistas e respondendo a perguntas de desafio. Ao acertarem, ganham pontos e descobrem geocaches virtuais. Quando atingem uma certa quantidade de pontos, podem criar e definir o seu próprio geocache em uma localização cultural relevante.
A aplicação promove o turismo e a educação cultural, incentivando os utilizadores a interagir com o património local de forma lúdica e educativa.

# Requisitos Funcionais:
- ### Registro de Utilizadores:
    O sistema deve permitir que os utilizadores se registem e criem um perfil.
    Deve incluir opções de login e recuperação de senha.
- ### Localização e Geolocalização:
    O sistema deve utilizar a localização GPS para determinar se o utilizador está a menos de X metros de um geocache.
    Integração com um mapa interativo para mostrar geocaches disponíveis.
- ### Sistema de Pistas:
    Os utilizadores devem receber pistas sobre onde estão localizados os geocaches culturais.
    Pistas devem estar vinculadas a um local histórico ou cultural.
- ### Resolução de Desafios:
    Cada geocache cultural deve estar vinculado a uma pergunta ou desafio que o utilizador precisa responder corretamente.
    Caso o desafio seja respondido corretamente, o utilizador recebe pontos e um geocache virtual.
- ### Acumulação de Pontos:
    O sistema deve calcular e acumular os pontos que os utilizadores ganham ao completarem desafios.
    Quando o utilizador atinge um número X de pontos, ele pode criar o seu próprio geocache cultural.
- ### Criação de Geocaches Culturais:
    Os utilizadores podem definir uma nova localização cultural para um geocache quando acumulam pontos suficientes.
    O sistema deve permitir que eles definam a pergunta/desafio para os outros jogadores.
- ### Exploração de Geocaches Criados por Outros:
    Os utilizadores devem poder explorar geocaches criados por outros jogadores, seguindo as pistas fornecidas.
- ### Sistema de Ranking:
    Um ranking deve ser implementado para mostrar os utilizadores com mais pontos acumulados ou geocaches criados.
- ### Notificações:
    O sistema deve notificar os utilizadores sobre novos geocaches disponíveis, desafios concluídos e outros eventos importantes.
- ### Histórico de Geocaches:
    Cada utilizador deve poder ver o histórico de geocaches encontrados e desafios resolvidos.

# APIs:
- Firebase Authentication : Para autenticação de utilizadores
- Google Maps API :  Para mostrar mapas e marcar geocaches.
- Google Places API : Para encontrar locais culturais para os geocaches.
- Firebase Cloud Messaging : Pode ser usado para enviar notificações push aos utilizadores
- (Testar) Geoapify? : Alternativa para geocodificação, localização, e criação de rotas. Ideal para calcular distâncias entre o utilizador e um geocache.

# Possíveis Nomes
- ## GeoCultura Explorer
- GeoExploradores Culturais
- Cultura em Movimento
