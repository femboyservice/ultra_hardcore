# ultra_hardcore
> refonte totale de mon 1e plugin :: l'uhc <br>
> de plus, on change pour enlever le loup-garou <br> <sub>je le referai plus tard (dans 7.5 ans)</sub>

## Commandes:
> [!NOTE]  
> Format des commandes
> - **/commande**
>   - sousCommande :: description de la sous commande
>     - action1 && action2 :: deux arguments faisant la même chose.
>     - actionName :: argument non-optionnel pour la commande
>     - *optionalAction* :: argument optionnel pour la commande
- **/uhc**
    - help && *null* :: renvoie au joueur toutes les sous commandes auxquelles il a access
        - *actionName* :: renvoie une description de *actionName*.
    - start :: active le cooldown pour commencer l'uhc
    - stop :: stop le cooldown (si il est actif)
    - setgroup <integer> :: change les groupes à <integer>
    - settings :: ouvre un inventaire avec tous les settings changeable de l'uhc
        - click droit :: pas fait :L
        - click gauche :: pas fait :P
- **/scenarios**
    - *null* :: donnes des informations sur les scenarios activées
    - help
        - *null* :: renvoie au joueur toutes les sous commandes auxquelles il a access
        - *scenarioName* :: renvoie une description du scenario et son fonctionnement

## config.yml
> [!NOTE]  
> Format des configurations
> - configName: <type: default> :: description
- lunarclientExclusif: <boolean: false> :: kick tout les joueurs n'utilisant pas lunar client.

----
### Developpé par femboyservice
- [discord](https://discord.com/users/1371531622960332851) :: femboyservice
- [namemc](https://fr.namemc.com/profile/femboysanslimite.1) :: femboyservice
- [e-z.bio](https://e-z.bio/service) :: service
----
> [!IMPORTANT]  
> ## Credits
> - [@NickNqck](https://github.com/NickNqck)
>   - critPatch & strengthPatch

