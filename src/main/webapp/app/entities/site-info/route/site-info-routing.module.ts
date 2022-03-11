import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SiteInfoComponent } from '../list/site-info.component';
import { SiteInfoDetailComponent } from '../detail/site-info-detail.component';
import { SiteInfoUpdateComponent } from '../update/site-info-update.component';
import { SiteInfoRoutingResolveService } from './site-info-routing-resolve.service';

const siteInfoRoute: Routes = [
  {
    path: '',
    component: SiteInfoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SiteInfoDetailComponent,
    resolve: {
      siteInfo: SiteInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SiteInfoUpdateComponent,
    resolve: {
      siteInfo: SiteInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SiteInfoUpdateComponent,
    resolve: {
      siteInfo: SiteInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(siteInfoRoute)],
  exports: [RouterModule],
})
export class SiteInfoRoutingModule {}
