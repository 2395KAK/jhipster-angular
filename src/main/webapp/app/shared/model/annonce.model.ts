export interface IAnnonce {
    id?: number;
    titre?: string;
    description?: string;
    prix?: number;
    categorie?: number;
    proprietaire?: string;
}

export class Annonce implements IAnnonce {
    constructor(
        public id?: number,
        public titre?: string,
        public description?: string,
        public prix?: number,
        public categorie?: number,
        public proprietaire?: string
    ) {}
}
