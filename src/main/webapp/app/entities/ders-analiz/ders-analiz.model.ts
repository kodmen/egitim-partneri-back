import { IBolum } from 'app/entities/bolum/bolum.model';
import { IKayit } from 'app/entities/kayit/kayit.model';

export interface IDersAnaliz {
  id?: number;
  toplamYanlis?: number | null;
  toplamDogru?: number | null;
  cozulenSoru?: number | null;
  tamamlandi?: boolean | null;
  aitOldBolum?: IBolum | null;
  kayitlar3s?: IKayit[] | null;
}

export class DersAnaliz implements IDersAnaliz {
  constructor(
    public id?: number,
    public toplamYanlis?: number | null,
    public toplamDogru?: number | null,
    public cozulenSoru?: number | null,
    public tamamlandi?: boolean | null,
    public aitOldBolum?: IBolum | null,
    public kayitlar3s?: IKayit[] | null
  ) {
    this.tamamlandi = this.tamamlandi ?? false;
  }
}

export function getDersAnalizIdentifier(dersAnaliz: IDersAnaliz): number | undefined {
  return dersAnaliz.id;
}
