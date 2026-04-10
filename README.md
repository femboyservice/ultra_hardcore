# ultra_hardcore
> refonte totale de mon 1e plugin :: l'uhc <br>
> de plus, on change pour enlever le loup-garou <br> <sub>je le referai plus tard (dans 7.5 ans)</sub>

## Commandes:
> [!NOTE]  
> Format des commandes
> - **/commande**
>   - sousCommande :: description de la sous commande
>     - actionName :: action faite par l'actionName
- **/uhc**
    - *null* :: donnes des informations sur l'uhc (% d'effet, limite de stuff, etc.)
    - help :: renvoie au joueur toutes les sous commandes auxquelles il a access
    - start :: active le cooldown pour commencer l'uhc
    - stop :: stop le cooldown (si il est actif)
    - settings :: ouvre un inventaire avec tous les settings changeable de l'uhc
        - click droit :: desactive le scenario
        - click gauche :: active le scenario
- **/scenarios**
    - *null* :: donnes des informations sur les scenarios activées
    - help
        - *null* :: renvoie au joueur toutes les sous commandes auxquelles il a access
        - *scenarioName* :: renvoie une description du scenario et son fonctionnement
    - list :: ouvre un inventaire avec tt les scenarios activable / desactivable
        - click gauche :: active / desactive le scenario
        - click droit :: informations et features supplementaire du scenario

## config.yml
> [!NOTE]  
> Format des configurations
> - configName: <type: default> :: description
- lunarclientExclusif: <boolean: false> :: kick tout les joueurs n'utilisant pas lunar client.

----
### Developpé par femboyservice
- [discord](https://discord.com/users/1371531622960332851) :: femboyservice
- [namemc](https://fr.namemc.com/profile/femboyservice.1) :: femboyservice
- [e-z.bio](https://e-z.bio/service) :: service
