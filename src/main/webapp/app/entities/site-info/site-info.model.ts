export interface ISiteInfo {
  id?: number;
  baslik?: string | null;
}

export class SiteInfo implements ISiteInfo {
  constructor(public id?: number, public baslik?: string | null) {}
}

export function getSiteInfoIdentifier(siteInfo: ISiteInfo): number | undefined {
  return siteInfo.id;
}
