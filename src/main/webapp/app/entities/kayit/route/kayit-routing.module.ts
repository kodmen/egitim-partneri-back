import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { KayitComponent } from '../list/kayit.component';
import { KayitDetailComponent } from '../detail/kayit-detail.component';
import { KayitUpdateComponent } from '../update/kayit-update.component';
import { KayitRoutingResolveService } from './kayit-routing-resolve.service';

const kayitRoute: Routes = [
  {
    path: '',
    component: KayitComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KayitDetailComponent,
    resolve: {
      kayit: KayitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KayitUpdateComponent,
    resolve: {
      kayit: KayitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KayitUpdateComponent,
    resolve: {
      kayit: KayitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(kayitRoute)],
  exports: [RouterModule],
})
export class KayitRoutingModule {}
