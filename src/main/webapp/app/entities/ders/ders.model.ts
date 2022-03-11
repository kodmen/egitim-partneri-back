import dayjs from 'dayjs/esm';
import { IMufredat } from 'app/entities/mufredat/mufredat.model';
import { IForm } from 'app/entities/form/form.model';
import { IKayit } from 'app/entities/kayit/kayit.model';
import { IOgretmen } from 'app/entities/ogretmen/ogretmen.model';

export interface IDers {
  id?: number;
  isim?: string | null;
  toplamPuan?: number | null;
  olusturulmaTarih?: dayjs.Dayjs | null;
  aciklama?: string | null;
  resimContentType?: string | null;
  resim?: string | null;
  dersMufredat?: IMufredat | null;
  dersForm?: IForm | null;
  kayitlar1s?: IKayit[] | null;
  dersOgretmeni?: IOgretmen | null;
}

export class Ders implements IDers {
  constructor(
    public id?: number,
    public isim?: string | null,
    public toplamPuan?: number | null,
    public olusturulmaTarih?: dayjs.Dayjs | null,
    public aciklama?: string | null,
    public resimContentType?: string | null,
    public resim?: string | null,
    public dersMufredat?: IMufredat | null,
    public dersForm?: IForm | null,
    public kayitlar1s?: IKayit[] | null,
    public dersOgretmeni?: IOgretmen | null
  ) {}
}

export function getDersIdentifier(ders: IDers): number | undefined {
  return ders.id;
}
